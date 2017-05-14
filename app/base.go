package main

import (
	"encoding/base64"
	"fmt"
	"os"
	"path"
	"strconv"

	"github.com/golang/protobuf/proto"
	"github.com/hyperledger/fabric-sdk-go/config"
	"github.com/hyperledger/fabric-sdk-go/fabric-client/events"
	fcUtil "github.com/hyperledger/fabric-sdk-go/fabric-client/helpers"

	fabricClient "github.com/hyperledger/fabric-sdk-go/fabric-client"
	bccspFactory "github.com/hyperledger/fabric/bccsp/factory"
	"github.com/hyperledger/fabric/protos/common"
	protos_utils "github.com/hyperledger/fabric/protos/utils"
)

// BaseSetupImpl implementation of BaseTestSetup
type BaseSetupImpl struct {
	Client          fabricClient.Client
	Chain           fabricClient.Chain
	EventHub        events.EventHub
	ConnectEventHub bool
	ConfigFile      string
	ChainID         string
	ChainCodeID     string
	Initialized     bool
	ChannelConfig   string
}

// Initialize reads configuration from file and sets up client, chain and event hub
func (setup *BaseSetupImpl) Initialize() error {

	if err := setup.InitConfig(); err != nil {
		return fmt.Errorf("Init from config failed: %v", err)
	}

	// Initialize bccsp factories before calling get client
	err := bccspFactory.InitFactories(&bccspFactory.FactoryOpts{
		ProviderName: "SW",
		SwOpts: &bccspFactory.SwOpts{
			HashFamily: config.GetSecurityAlgorithm(),
			SecLevel:   config.GetSecurityLevel(),
			FileKeystore: &bccspFactory.FileKeystoreOpts{
				KeyStorePath: config.GetKeyStorePath(),
			},
			Ephemeral: false,
		},
	})
	if err != nil {
		return fmt.Errorf("Failed getting ephemeral software-based BCCSP [%s]", err)
	}

	client, err := fcUtil.GetClient("admin", "adminpw", "/tmp/enroll_user")
	if err != nil {
		return fmt.Errorf("Create client failed: %v", err)
	}
	setup.Client = client

	chain, err := fcUtil.GetChain(setup.Client, setup.ChainID)
	if err != nil {
		return fmt.Errorf("Create chain (%s) failed: %v", setup.ChainID, err)
	}
	setup.Chain = chain

	// Create and join channel
	if err := fcUtil.CreateAndJoinChannel(client, chain, setup.ChannelConfig); err != nil {
		return fmt.Errorf("CreateAndJoinChannel return error: %v", err)
	}

	eventHub, err := getEventHub()
	if err != nil {
		return err
	}

	if setup.ConnectEventHub {
		if err := eventHub.Connect(); err != nil {
			return fmt.Errorf("Failed eventHub.Connect() [%s]", err)
		}
	}
	setup.EventHub = eventHub

	setup.Initialized = true

	return nil
}

// InitConfig ...
func (setup *BaseSetupImpl) InitConfig() error {
	if err := config.InitConfig(setup.ConfigFile); err != nil {
		return err
	}
	return nil
}

// InstantiateCC ...
func (setup *BaseSetupImpl) InstantiateCC(chainCodeID string, chainID string, chainCodePath string, chainCodeVersion string, args []string) error {
	if err := fcUtil.SendInstantiateCC(setup.Chain, chainCodeID, chainID, args, chainCodePath, chainCodeVersion, []fabricClient.Peer{setup.Chain.GetPrimaryPeer()}, setup.EventHub); err != nil {
		return err
	}
	return nil
}

// InstallCC ...
func (setup *BaseSetupImpl) InstallCC(chainCodeID string, chainCodePath string, chainCodeVersion string, chaincodePackage []byte) error {
	if err := fcUtil.SendInstallCC(setup.Client, setup.Chain, chainCodeID, chainCodePath, chainCodeVersion, chaincodePackage, setup.Chain.GetPeers(), setup.GetDeployPath()); err != nil {
		return fmt.Errorf("SendInstallProposal return error: %v", err)
	}
	return nil
}

// GetDeployPath ..
func (setup *BaseSetupImpl) GetDeployPath() string {
	pwd, _ := os.Getwd()
	return path.Join(pwd, "../chaincode")
}

// Invoke ...
func (setup *BaseSetupImpl) Invoke(chainID string, chainCodeID string, args []string) (string, error) {
	transactionProposalResponse, txID, err := fcUtil.CreateAndSendTransactionProposal(setup.Chain, setup.ChainCodeID, setup.ChainID, args, []fabricClient.Peer{setup.Chain.GetPrimaryPeer()}, nil)
	if err != nil {
		return "", fmt.Errorf("CreateAndSendTransactionProposal return error: %v", err)
	}

	_, err = fcUtil.CreateAndSendTransaction(setup.Chain, transactionProposalResponse)
	if err != nil {
		return "", fmt.Errorf("CreateAndSendTransaction return error: %v", err)
	}

	return txID, nil
}

// Query ...
func (setup *BaseSetupImpl) Query(chainID string, chainCodeID string, args []string) (string, error) {
	transactionProposalResponses, _, err := fcUtil.CreateAndSendTransactionProposal(setup.Chain, chainCodeID, chainID, args, []fabricClient.Peer{setup.Chain.GetPrimaryPeer()}, nil)
	if err != nil {
		return "", fmt.Errorf("CreateAndSendTransactionProposal return error: %v", err)
	}
	return string(transactionProposalResponses[0].GetResponsePayload()), nil
}

// getEventHub initilizes the event hub
func getEventHub() (events.EventHub, error) {
	eventHub := events.NewEventHub()
	foundEventHub := false
	peerConfig, err := config.GetPeersConfig()
	if err != nil {
		return nil, fmt.Errorf("Error reading peer config: %v", err)
	}
	for _, p := range peerConfig {
		if p.EventHost != "" && p.EventPort != 0 {
			fmt.Printf("******* EventHub connect to peer (%s:%d) *******\n", p.EventHost, p.EventPort)
			eventHub.SetPeerAddr(fmt.Sprintf("%s:%d", p.EventHost, p.EventPort),
				p.TLS.Certificate, p.TLS.ServerHostOverride)
			foundEventHub = true
			break
		}
	}

	if !foundEventHub {
		return nil, fmt.Errorf("No EventHub configuration found")
	}

	return eventHub, nil
}

// Blockchain services ---

// GetPeers ...
func (setup *BaseSetupImpl) GetPeers(chainID string, chainCodeID string, args []string) (Peers, error) {
	var peersChain Peers
	peers := setup.Chain.GetPeers()

	fmt.Printf("Name=%v, pp=%v, peers=%v, orderer=%v\n", setup.Chain.GetName(), setup.Chain.GetPrimaryPeer(), setup.Chain.GetPeers(), setup.Chain.GetOrderers())

	for i, p := range peers {
		var peer Peer
		peer.ID.Name = p.GetName()
		if peer.ID.Name == "" {
			peer.ID.Name = fmt.Sprintf("%v", i)
		}
		peer.Address = p.GetURL()
		peer.PkiID = p.GetName()

		peersChain.Peers = append(peersChain.Peers, peer)
	}

	return peersChain, nil
}

// GetBlockchainInfo ...
func (setup *BaseSetupImpl) GetBlockchainInfo(chainID string, chainCodeID string, args []string) (BlockchainInfo, error) {
	var blockchainInfo BlockchainInfo
	b, err := setup.Chain.QueryInfo()
	if err != nil {
		return blockchainInfo, err
	}
	blockchainInfo.Height = b.Height
	blockchainInfo.CurrentBlockHash = base64.StdEncoding.EncodeToString(b.CurrentBlockHash)
	blockchainInfo.PreviousBlockHash = base64.StdEncoding.EncodeToString(b.PreviousBlockHash)

	return blockchainInfo, nil
}

// GetBlock ...
func (setup *BaseSetupImpl) GetBlock(chainID string, chainCodeID string, args []string) (Block, error) {
	var block Block
	block.Transactions = make([]Transaction, 0)

	if len(args) < 1 {
		return block, nil
	}
	num, err := strconv.Atoi(args[0])
	if err != nil {
		return block, err
	}
	b, err := setup.Chain.QueryBlock(num)
	if err != nil {
		return block, nil
	}
	block.StateHash = base64.StdEncoding.EncodeToString(b.Header.DataHash)
	block.PreviousBlockHash = base64.StdEncoding.EncodeToString(b.Header.PreviousHash)

	// parse block
	envelope, err := protos_utils.ExtractEnvelope(b, 0)
	if err != nil {
		return block, err
	}

	payload, err := protos_utils.ExtractPayload(envelope)
	if err != nil {
		return block, err
	}

	channelHeader, err := protos_utils.UnmarshalChannelHeader(payload.Header.ChannelHeader)
	if err != nil {
		return block, err
	}

	block.Version = int(common.HeaderType(channelHeader.Type))
	switch common.HeaderType(channelHeader.Type) {
	case common.HeaderType_MESSAGE:
		break
	case common.HeaderType_CONFIG:
		configEnvelope := &common.ConfigEnvelope{}
		if err := proto.Unmarshal(payload.Data, configEnvelope); err != nil {
			return block, err
		}
		break
	case common.HeaderType_CONFIG_UPDATE:
		configUpdateEnvelope := &common.ConfigUpdateEnvelope{}
		if err := proto.Unmarshal(payload.Data, configUpdateEnvelope); err != nil {
			return block, err
		}
		break
	case common.HeaderType_ENDORSER_TRANSACTION:
		tx, err := protos_utils.GetTransaction(payload.Data)
		if err != nil {
			return block, err
		}

		channelHeader := &common.ChannelHeader{}
		if err := proto.Unmarshal(payload.Header.ChannelHeader, channelHeader); err != nil {
			return block, err
		}

		signatureHeader := &common.SignatureHeader{}
		if err := proto.Unmarshal(payload.Header.SignatureHeader, signatureHeader); err != nil {
			return block, err
		}

		for _, action := range tx.Actions {
			var transaction Transaction
			transaction.ChaincodeID = chainCodeID
			transaction.Payload = base64.StdEncoding.EncodeToString(action.Payload)

			transaction.Type = channelHeader.Type
			transaction.UUID = channelHeader.TxId
			transaction.Cert = base64.StdEncoding.EncodeToString(signatureHeader.Creator)
			transaction.Signature = base64.StdEncoding.EncodeToString(envelope.Signature)
			if channelHeader != nil && channelHeader.Timestamp != nil {
				transaction.Timestamp.Seconds = channelHeader.Timestamp.Seconds
				transaction.Timestamp.Nanos = channelHeader.Timestamp.Nanos
			}

			block.Transactions = append(block.Transactions, transaction)
		}
		break
	case common.HeaderType_ORDERER_TRANSACTION:
		break
	case common.HeaderType_DELIVER_SEEK_INFO:
		break
	default:
		return block, fmt.Errorf("Unknown message")
	}

	return block, nil
}

// GetTransaction ...
func (setup *BaseSetupImpl) GetTransaction(chainID string, chainCodeID string, args []string) (Transaction, error) {
	var transaction Transaction

	if len(args) < 1 {
		return transaction, nil
	}
	t, err := setup.Chain.QueryTransaction(args[0])
	if err != nil {
		return transaction, err
	}

	payload, err := protos_utils.ExtractPayload(t.TransactionEnvelope)
	if err != nil {
		return transaction, err
	}

	channelHeader, err := protos_utils.UnmarshalChannelHeader(payload.Header.ChannelHeader)
	if err != nil {
		return transaction, err
	}

	switch common.HeaderType(channelHeader.Type) {
	case common.HeaderType_MESSAGE:
		break
	case common.HeaderType_CONFIG:
		configEnvelope := &common.ConfigEnvelope{}
		if err := proto.Unmarshal(payload.Data, configEnvelope); err != nil {
			return transaction, err
		}
		break
	case common.HeaderType_CONFIG_UPDATE:
		configUpdateEnvelope := &common.ConfigUpdateEnvelope{}
		if err := proto.Unmarshal(payload.Data, configUpdateEnvelope); err != nil {
			return transaction, err
		}
		break
	case common.HeaderType_ENDORSER_TRANSACTION:
		tx, err := protos_utils.GetTransaction(payload.Data)
		if err != nil {
			return transaction, err
		}

		channelHeader := &common.ChannelHeader{}
		if err := proto.Unmarshal(payload.Header.ChannelHeader, channelHeader); err != nil {
			return transaction, err
		}

		signatureHeader := &common.SignatureHeader{}
		if err := proto.Unmarshal(payload.Header.SignatureHeader, signatureHeader); err != nil {
			return transaction, err
		}

		for i, action := range tx.Actions {
			fmt.Printf("GetTransaction: index=%v\n", i)
			transaction.ChaincodeID = chainCodeID
			transaction.Payload = base64.StdEncoding.EncodeToString(action.Payload)

			transaction.Type = channelHeader.Type
			transaction.UUID = channelHeader.TxId
			transaction.Cert = base64.StdEncoding.EncodeToString(signatureHeader.Creator)
			transaction.Signature = base64.StdEncoding.EncodeToString(t.TransactionEnvelope.Signature)
			if channelHeader != nil && channelHeader.Timestamp != nil {
				transaction.Timestamp.Seconds = channelHeader.Timestamp.Seconds
				transaction.Timestamp.Nanos = channelHeader.Timestamp.Nanos
			}
		}
		break
	case common.HeaderType_ORDERER_TRANSACTION:
		break
	case common.HeaderType_DELIVER_SEEK_INFO:
		break
	default:
		return transaction, fmt.Errorf("Unknown message")
	}

	return transaction, nil
}
