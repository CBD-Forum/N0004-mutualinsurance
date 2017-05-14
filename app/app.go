package main

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"strings"

	"net/http"

	"github.com/spf13/cobra"

	"github.com/gocraft/web"
	"github.com/spf13/viper"
)

// --------------- AppCmd ---------------

// AppCmd returns the cobra command for APP
func AppCmd() *cobra.Command {
	return appStartCmd
}

var (
	chainCodeID      = "pinganinsurance"
	chainCodePath    = "github.com/chainnova/mutualinsurance/chaincode"
	chainCodeVersion = "v3"
	ccAdaptor        BaseSetupImpl

	appStartCmd = &cobra.Command{
		Use:   "start",
		Short: "Starts the app.",
		Long:  `Starts a app that interacts with the network.`,
		RunE: func(cmd *cobra.Command, args []string) error {
			return serve(args)
		},
	}
)

// --------------- Structs ---------------

type Status struct {
	Code    int    `protobuf:"bytes,1,opt,name=code" json:"code"`
	Message string `protobuf:"bytes,2,opt,name=message" json:"message"`
}

type Return struct {
	Status Status      `protobuf:"bytes,1,opt,name=status" json:"status"`
	Data   interface{} `protobuf:"bytes,2,opt,name=data" json:"data,omitempty"`
}

type Profile struct {
	ID          string      `protobuf:"bytes,1,opt,name=id" json:"id"`
	Balance     float64     `protobuf:"bytes,2,opt,name=balance" json:"balance"`
	CountHelped int         `protobuf:"bytes,3,opt,name=count_helped" json:"count_helped"`
	Fee         float64     `protobuf:"bytes,4,opt,name=fee" json:"fee"`
	Insurances  []Insurance `protobuf:"bytes,5,opt,name=insurances" json:"insurances"`
}

type Charge struct {
	IDUser string `protobuf:"bytes,1,opt,name=id_user" json:"id_user"`
	Amount string `protobuf:"bytes,2,opt,name=amount" json:"amount"`
}

type Insurance struct {
	ID          string         `protobuf:"bytes,1,opt,name=id" json:"id"`
	Name        string         `protobuf:"bytes,2,opt,name=name" json:"name"`
	TimeFounded string         `protobuf:"bytes,3,opt,name=time_founded" json:"time_founded"`
	Description []string       `protobuf:"bytes,4,opt,name=description" json:"description"`
	Issuer      string         `protobuf:"bytes,5,opt,name=issuer" json:"issuer"`
	Source      string         `protobuf:"bytes,6,opt,name=source" json:"source"`
	AmountMax   string         `protobuf:"bytes,7,opt,name=amount_max" json:"amount_max"`
	CountBought string         `protobuf:"bytes,8,opt,name=count_bought" json:"count_bought"`
	CountHelped int            `protobuf:"bytes,9,opt,name=count_helped" json:"count_helped"`
	Balance     string         `protobuf:"bytes,10,opt,name=balance" json:"balance"`
	Amount      float64        `protobuf:"bytes,11,opt,name=amount" json:"amount"`
	Fee         string         `protobuf:"bytes,12,opt,name=fee" json:"fee"`
	Bought      bool           `protobuf:"bytes,13,opt,name=bought" json:"bought"`
	Claims      []Claim        `protobuf:"bytes,14,opt,name=claims" json:"claims"`
	Statistics  []InsuranceSta `protobuf:"bytes,15,opt,name=statistics" json:"statistics"`
}

type InsuranceSta struct {
	Month       string  `protobuf:"bytes,1,opt,name=month" json:"month"`
	CountNew    int     `protobuf:"bytes,2,opt,name=count_new" json:"count_new"`
	CountQuit   int     `protobuf:"bytes,3,opt,name=count_quit" json:"count_quit"`
	CountHelped int     `protobuf:"bytes,4,opt,name=count_helped" json:"count_helped"`
	Amount      float64 `protobuf:"bytes,5,opt,name=amount" json:"amount"`
	Cost        float64 `protobuf:"bytes,6,opt,name=cost" json:"cost"`
	Fee         float64 `protobuf:"bytes,7,opt,name=fee" json:"fee"`
}

type InsuranceReq struct {
	IDInsurance string `protobuf:"bytes,1,opt,name=id_insurance" json:"id_insurance,omitempty"`
	IDUser      string `protobuf:"bytes,2,opt,name=id_user" json:"id_user,omitempty"`
	Name        string `protobuf:"bytes,3,opt,name=name" json:"name,omitempty"`
	IDCard      string `protobuf:"bytes,4,opt,name=id_card" json:"id_card,omitempty"`
	Mobile      string `protobuf:"bytes,5,opt,name=mobile" json:"mobile,omitempty"`
	Amount      string `protobuf:"bytes,6,opt,name=amount" json:"amount,omitempty"` //float64
	Medical     string `protobuf:"bytes,7,opt,name=medical" json:"medical,omitempty"`
	IDDriver    string `protobuf:"bytes,8,opt,name=id_driver" json:"id_driver,omitempty"`
	IDDriving   string `protobuf:"bytes,9,opt,name=id_driving" json:"id_driving,omitempty"`
	IDDidi      string `protobuf:"bytes,10,opt,name=id_didi" json:"id_didi,omitempty"`
	IDCsa       string `protobuf:"bytes,11,opt,name=id_csa" json:"id_csa,omitempty"`
	TimeBought  string `protobuf:"bytes,12,opt,name=time_bought" json:"time_bought,omitempty"`
	Reserved    string `protobuf:"bytes,13,opt,name=reserved" json:"reserved,omitempty"`
}

type Claim struct {
	IDInsurance   string  `protobuf:"bytes,1,opt,name=id_insurance" json:"id_insurance"`
	IDUser        string  `protobuf:"bytes,2,opt,name=id_user" json:"id_user"`
	IDClaim       string  `protobuf:"bytes,3,opt,name=id_claim" json:"id_claim"`
	Name          string  `protobuf:"bytes,4,opt,name=name" json:"name"`
	Mobile        string  `protobuf:"bytes,5,opt,name=mobile" json:"mobile"`
	CardNum       string  `protobuf:"bytes,6,opt,name=cardnum" json:"cardnum"`
	Status        string  `protobuf:"bytes,7,opt,name=status" json:"status"`
	Amount        float64 `protobuf:"bytes,8,opt,name=amount" json:"amount"`
	ReportThird   string  `protobuf:"bytes,9,opt,name=report_third" json:"report_third"`
	ReportVisitor string  `protobuf:"bytes,10,opt,name=report_visitor" json:"report_visitor"`
	Receipt       string  `protobuf:"bytes,11,opt,name=receipt" json:"receipt"`
	TimeClaimed   string  `protobuf:"bytes,12,opt,name=time_claimed" json:"time_claimed"`
	TimeApproved  string  `protobuf:"bytes,13,opt,name=time_approved" json:"time_approved"`
}

// Blockchain
type Peers struct {
	Peers []Peer `protobuf:"bytes,1,opt,name=peers" json:"peers"`
}

type Peer struct {
	ID      ID     `protobuf:"bytes,1,opt,name=id" json:"id"`
	Address string `protobuf:"bytes,2,opt,name=address" json:"address"`
	Type    int    `protobuf:"bytes,3,opt,name=type" json:"type"`
	PkiID   string `protobuf:"bytes,4,opt,name=pkiID" json:"pkiID,omitempty"`
}

type ID struct {
	Name string `protobuf:"bytes,1,opt,name=name" json:"name"`
}

type BlockchainInfo struct {
	Height            uint64 `protobuf:"bytes,1,opt,name=height" json:"height"`
	CurrentBlockHash  string `protobuf:"bytes,2,opt,name=currentBlockHash" json:"currentBlockHash"`
	PreviousBlockHash string `protobuf:"bytes,3,opt,name=previousBlockHash" json:"previousBlockHash"`
}

type Block struct {
	Version           int                        `protobuf:"bytes,1,opt,name=version" json:"version"`
	Timestamp         string                     `protobuf:"bytes,2,opt,name=timestamp" json:"timestamp"`
	Transactions      []Transaction              `protobuf:"bytes,3,opt,name=transactions" json:"transactions"`
	StateHash         string                     `protobuf:"bytes,4,opt,name=stateHash" json:"stateHash"`
	PreviousBlockHash string                     `protobuf:"bytes,5,opt,name=previousBlockHash" json:"previousBlockHash"`
	NonHashData       LocalLedgerCommitTimestamp `protobuf:"bytes,6,opt,name=nonHashData" json:"nonHashData,omitempty"`
}

type LocalLedgerCommitTimestamp struct {
	LocalLedgerCommitTimestamp Timestamp `protobuf:"bytes,1,opt,name=localLedgerCommitTimestamp" json:"localLedgerCommitTimestamp,omitempty"`
}

type Timestamp struct {
	Seconds int64 `protobuf:"bytes,1,opt,name=seconds" json:"seconds"`
	Nanos   int32 `protobuf:"bytes,2,opt,name=nanos" json:"nanos"`
}

type Transaction struct {
	Type        int32     `protobuf:"bytes,1,opt,name=type" json:"type"`
	ChaincodeID string    `protobuf:"bytes,2,opt,name=chaincodeID" json:"chaincodeID"`
	Payload     string    `protobuf:"bytes,3,opt,name=payload" json:"payload"`
	UUID        string    `protobuf:"bytes,4,opt,name=uuid" json:"uuid"`
	Timestamp   Timestamp `protobuf:"bytes,5,opt,name=timestamp" json:"timestamp"`
	Cert        string    `protobuf:"bytes,6,opt,name=cert" json:"cert"`
	Signature   string    `protobuf:"bytes,7,opt,name=signature" json:"signature"`
}

// --------------- InsuranceAPP ---------------

// InsuranceAPP defines the Insurance REST service object.
type InsuranceAPP struct {
}

func buildInsuranceRouter() *web.Router {
	router := web.New(InsuranceAPP{})

	// Add middleware
	router.Middleware((*InsuranceAPP).setResponseType)

	// Add not found page
	router.NotFound((*InsuranceAPP).NotFound)

	// ---- blockchain router ----
	blockchain := router.Subrouter(InsuranceAPP{}, "/v1")

	// Block chain
	blockchain.Get("/network/peers", (*InsuranceAPP).getPeers)
	blockchain.Get("/chain", (*InsuranceAPP).getBlockchain)
	blockchain.Get("/chain/blocks/:id", (*InsuranceAPP).getBlock)
	blockchain.Get("/transactions/:id", (*InsuranceAPP).getTransaction)

	// --- app router ----
	app := router.Subrouter(InsuranceAPP{}, "/v1")

	// Add middleware
	app.Middleware((*InsuranceAPP).basicAuthenticate)

	// Add routes
	app.Get("/login", (*InsuranceAPP).login)
	app.Post("/insurance", (*InsuranceAPP).publishInsurance)
	app.Get("/insurance", (*InsuranceAPP).getInsurances)
	app.Get("/insurance/:id", (*InsuranceAPP).getInsurance)
	app.Post("/request", (*InsuranceAPP).buyInsurance)
	app.Post("/claim", (*InsuranceAPP).claimInsurance)
	app.Get("/claim/:id", (*InsuranceAPP).getClaim)
	app.Post("/approve", (*InsuranceAPP).approveInsurance)
	app.Get("/me", (*InsuranceAPP).getProfile)
	app.Post("/charge", (*InsuranceAPP).charge)

	return router
}

// basicAuthenticate basic authentication
func (s *InsuranceAPP) basicAuthenticate(rw web.ResponseWriter, req *web.Request, next web.NextMiddlewareFunc) {
	const basicScheme string = "Basic "

	// Confirm the request is sending Basic Authentication credentials.
	auth := req.Header.Get("Authorization")
	if !strings.HasPrefix(auth, basicScheme) {
		logger.Errorf("authentication error: scheme=%v", auth)
		return
	}

	// Get the plain-text username and password from the request.
	// The first six characters are skipped - e.g. "Basic ".
	str, err := base64.StdEncoding.DecodeString(auth[len(basicScheme):])
	if err != nil {
		logger.Errorf("authentication error: auth=%v", str)
		return
	}

	// Split on the first ":" character only, with any subsequent colons assumed to be part
	// of the password. Note that the RFC2617 standard does not place any limitations on
	// allowable characters in the password.
	creds := bytes.SplitN(str, []byte(":"), 2)

	if len(creds) != 2 {
		logger.Errorf("authentication error: creds=%v", creds)
		return
	}

	user := string(creds[0])
	pass := string(creds[1])

	// TODO: check user and pass

	// Set header for later use
	req.Header.Set("user", user)
	req.Header.Set("pass", pass)
	logger.Infof("basic authentication: user=%v, pass=%v", user, pass)

	next(rw, req)
}

// setResponseType is a middleware function that sets the appropriate response
// headers. Currently, it is setting the "Content-Type" to "application/json" as
// well as the necessary headers in order to enable CORS for Swagger usage.
func (s *InsuranceAPP) setResponseType(rw web.ResponseWriter, req *web.Request, next web.NextMiddlewareFunc) {
	rw.Header().Set("Content-Type", "application/json")

	// Enable CORS
	rw.Header().Set("Access-Control-Allow-Origin", "*")
	rw.Header().Set("Access-Control-Allow-Headers", "accept, content-type")

	next(rw, req)
}

// NotFound returns a custom landing page when a given hyperledger end point
// had not been defined.
func (s *InsuranceAPP) NotFound(rw web.ResponseWriter, r *web.Request) {
	rw.WriteHeader(http.StatusNotFound)
	json.NewEncoder(rw).Encode(Status{Code: -1, Message: "Insurance endpoint not found."})
}

// login confirms the account and secret password of the client with the
// CA and stores the enrollment certificate and key in the Devops server.
func (s *InsuranceAPP) login(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	user := req.Header.Get("user")
	pass := req.Header.Get("pass")

	ret.Status.Code = 0
	ret.Status.Message = fmt.Sprintf("Login successful for %v", user)

	rw.WriteHeader(http.StatusOK)
	encoder.Encode(ret)
	logger.Infof("Login successful: user=%v, pass=%v", user, pass)

	return
}

// publishInsurance publishInsurance
func (s *InsuranceAPP) publishInsurance(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	// Decode the incoming JSON payload
	body, err := ioutil.ReadAll(req.Body)
	if err != nil {
		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(Status{Code: -1, Message: err.Error()})
		logger.Errorf("Error: %s", err)

		return
	}

	var insurance Insurance
	err = json.Unmarshal(body, &insurance)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}
	logger.Infof("publishInsurance: '%s'.\n", insurance)

	args := []string{"publishInsurance", string(body)}
	txID, err := ccAdaptor.Invoke(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = fmt.Sprintf("%v", txID)

	encoder.Encode(ret)
	logger.Infof("publishInsurance: '%s'\n", txID)

	return
}

// GetInsurances GetInsurances
func (s *InsuranceAPP) getInsurances(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	user := req.Header.Get("user")
	args := []string{"getInsurances", user}

	result, err := ccAdaptor.Query(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	if result == "" {
		ret.Status.Code = 0
		ret.Status.Message = ""
		ret.Data = make([]Insurance, 0)

		encoder.Encode(ret)
		logger.Infof("getInsurances: '%s'\n", "")

		return
	}

	var insurances []Insurance
	err = json.Unmarshal([]byte(result), &insurances)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = ""
	if insurances != nil {
		ret.Data = insurances
	} else {
		ret.Data = make([]Insurance, 0)
	}

	encoder.Encode(ret)
	logger.Infof("getInsurances: '%s'\n", insurances)

	return
}

// GetClaim GetClaim
func (s *InsuranceAPP) getInsurance(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	user := req.Header.Get("user")
	id := req.PathParams["id"]
	args := []string{"getInsurance", user, id}

	result, err := ccAdaptor.Query(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	if result == "" {
		ret.Status.Code = 0
		ret.Status.Message = ""

		encoder.Encode(ret)
		logger.Infof("getInsurance: '%s'\n", "")

		return
	}

	var insurance Insurance
	err = json.Unmarshal([]byte(result), &insurance)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = ""
	ret.Data = insurance

	encoder.Encode(ret)
	logger.Infof("getInsurance: '%s'\n", result)

	return
}

// buyInsurancee buyInsurancee
func (s *InsuranceAPP) buyInsurance(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	user := req.Header.Get("user")
	if user == "" {
		ret.Status.Code = -1
		ret.Status.Message = "No user specified"

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)

		return
	}

	// Decode the incoming JSON payload
	body, err := ioutil.ReadAll(req.Body)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	var insuranceReq InsuranceReq
	err = json.Unmarshal(body, &insuranceReq)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}
	insuranceReq.IDUser = user
	logger.Infof("buyInsurance: '%s'.\n", insuranceReq)

	body, err = json.Marshal(insuranceReq)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	args := []string{"buyInsurance", user, string(body)}
	txID, err := ccAdaptor.Invoke(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = fmt.Sprintf("%v", txID)

	encoder.Encode(ret)
	logger.Infof("buyInsurance: '%s'.\n", txID)

	return
}

// claimInsurancee claimInsurancee
func (s *InsuranceAPP) claimInsurance(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	// Decode the incoming JSON payload
	body, err := ioutil.ReadAll(req.Body)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	var claim Claim
	err = json.Unmarshal(body, &claim)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}
	claim.IDUser = req.Header.Get("user")
	logger.Infof("claimInsurance: '%s'.\n", claim)

	body, err = json.Marshal(claim)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	args := []string{"claimInsurance", string(body)}
	txID, err := ccAdaptor.Invoke(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = fmt.Sprintf("%v", txID)

	encoder.Encode(ret)
	logger.Infof("claimInsurancee: '%s'.\n", txID)

	return
}

// getClaimm getClaimm
func (s *InsuranceAPP) getClaim(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	id := req.PathParams["id"]
	args := []string{"getClaim", id}

	result, err := ccAdaptor.Query(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	if result == "" {
		ret.Status.Code = 0
		ret.Status.Message = ""

		encoder.Encode(ret)
		logger.Infof("getInsurance: '%s'\n", "")

		return
	}

	var claim Claim
	err = json.Unmarshal([]byte(result), &claim)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = ""
	ret.Data = claim

	encoder.Encode(ret)
	logger.Infof("getClaim: '%s'\n", result)

	return
}

// approveInsurance approveInsurance
func (s *InsuranceAPP) approveInsurance(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	// Decode the incoming JSON payload
	body, err := ioutil.ReadAll(req.Body)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	var claim Claim
	err = json.Unmarshal(body, &claim)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}
	claim.IDUser = req.Header.Get("user")
	logger.Infof("approveInsurance: '%s'.\n", claim)
	if claim.Status != "approved" && claim.Status != "rejected" {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	body, err = json.Marshal(claim)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	args := []string{"approveInsurance", string(body)}
	txID, err := ccAdaptor.Invoke(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = fmt.Sprintf("%v", txID)

	encoder.Encode(ret)
	logger.Infof("approveInsurance: '%s'.\n", txID)

	return
}

// getPeers getPeers
func (s *InsuranceAPP) getProfile(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	user := req.Header.Get("user")
	args := []string{"getProfile", user}

	result, err := ccAdaptor.Query(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	if result == "" {
		ret.Status.Code = 0
		ret.Status.Message = ""

		encoder.Encode(ret)
		logger.Infof("getProfile: '%s'\n", "")

		return
	}

	var profile Profile
	err = json.Unmarshal([]byte(result), &profile)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = ""
	ret.Data = profile

	encoder.Encode(ret)
	logger.Infof("getProfile: '%s'\n", result)

	return
}

// Blockchain service

// getPeers getPeers
func (s *InsuranceAPP) getPeers(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	result, err := ccAdaptor.GetPeers(ccAdaptor.ChainID, chainCodeID, nil)
	if err != nil {
		var ret Return
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	encoder.Encode(result)
	logger.Infof("getPeers: '%s'\n", result)

	return
}

// getBlockchain getBlockchain
func (s *InsuranceAPP) getBlockchain(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	result, err := ccAdaptor.GetBlockchainInfo(ccAdaptor.ChainID, chainCodeID, nil)
	if err != nil {
		var ret Return
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	encoder.Encode(result)
	logger.Infof("getBlockchain: '%s'\n", result)

	return
}

// getBlock getBlock
func (s *InsuranceAPP) getBlock(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)

	id := req.PathParams["id"]
	args := []string{id}
	result, err := ccAdaptor.GetBlock(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		var ret Return
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	encoder.Encode(result)
	logger.Infof("getBlock: '%s'\n", result)

	return
}

// getTransaction getTransaction
func (s *InsuranceAPP) getTransaction(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)

	id := req.PathParams["id"]
	args := []string{id}
	result, err := ccAdaptor.GetTransaction(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		var ret Return
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	encoder.Encode(result)
	logger.Infof("getTransaction: '%s'\n", result)

	return
}

// StartInsuranceServer initializes the REST service and adds the required
// middleware and routes.
func startInsuranceServer() {
	// Initialize the REST service object
	tlsEnabled := viper.GetBool("app.tls.enabled")

	logger.Infof("Initializing the REST service on %s, TLS is %s.", viper.GetString("app.address"), (map[bool]string{true: "enabled", false: "disabled"})[tlsEnabled])

	router := buildInsuranceRouter()

	// Start server
	if tlsEnabled {
		err := http.ListenAndServeTLS(viper.GetString("app.address"), viper.GetString("app.tls.cert.file"), viper.GetString("app.tls.key.file"), router)
		if err != nil {
			logger.Errorf("ListenAndServeTLS: %s", err)
		}
	} else {
		err := http.ListenAndServe(viper.GetString("app.address"), router)
		if err != nil {
			logger.Errorf("ListenAndServe: %s", err)
		}
	}
}

func (s *InsuranceAPP) charge(rw web.ResponseWriter, req *web.Request) {
	encoder := json.NewEncoder(rw)
	var ret Return

	body, err := ioutil.ReadAll(req.Body)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	var charge Charge
	err = json.Unmarshal(body, &charge)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	args := []string{"charge", string(body)}
	txID, err := ccAdaptor.Invoke(ccAdaptor.ChainID, chainCodeID, args)
	if err != nil {
		ret.Status.Code = -1
		ret.Status.Message = err.Error()

		rw.WriteHeader(http.StatusBadRequest)
		encoder.Encode(ret)
		logger.Errorf("Error: %s", err)

		return
	}

	ret.Status.Code = 0
	ret.Status.Message = fmt.Sprintf("%v", txID)

	encoder.Encode(ret)
	logger.Infof("approveInsurance: '%s'.\n", txID)

	return
}

// start serve
func serve(args []string) error {
	// Create and register the REST service if configured
	startInsuranceServer()

	logger.Infof("Starting app...")

	return nil
}
