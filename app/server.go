package main

import (
	"fmt"
	"os"
	"runtime"
	"strings"

	logging "github.com/op/go-logging"
	"github.com/spf13/cobra"
	"github.com/spf13/viper"

	"github.com/hyperledger/fabric/common/flogging"
)

const (
	cmdRoot = "core"
)

var (
	// Logging
	logger = logging.MustGetLogger("multualinsurance.app")

	versionFlag bool
)

// The main command describes the service and
// defaults to printing the help message.
var mainCmd = &cobra.Command{
	Use: "app",
	PersistentPreRunE: func(cmd *cobra.Command, args []string) error {
		loggingSpec := viper.GetString("logging_level")

		if loggingSpec == "" {
			loggingSpec = "DEBUG"
		}
		flogging.InitFromSpec(loggingSpec)

		return nil
	},
	Run: func(cmd *cobra.Command, args []string) {
		if versionFlag {
			VersionPrint()
		} else {
			cmd.HelpFunc()(cmd, args)
		}
	},
}

func main() {
	// Logging
	var formatter = logging.MustStringFormatter(
		`%{color}[%{module}] %{shortfunc} [%{shortfile}] -> %{level:.4s} %{id:03x}%{color:reset} %{message}`,
	)
	logging.SetFormatter(formatter)

	// viper init
	viper.AddConfigPath("./")
	viper.SetConfigName(cmdRoot)
	viper.SetEnvPrefix(cmdRoot)
	viper.AutomaticEnv()
	replacer := strings.NewReplacer(".", "_")
	viper.SetEnvKeyReplacer(replacer)
	err := viper.ReadInConfig()
	if err != nil {
		panic(fmt.Errorf("Fatal error config file: %s \n", err))
	}

	// chaincode setup
	ccAdaptor = BaseSetupImpl{
		ConfigFile:      "../fixtures/config/config_test.yaml",
		ChainID:         "testchannel",
		ChannelConfig:   "../fixtures/channel/testchannel.tx",
		ConnectEventHub: true,
		ChainCodeID:     chainCodeID,
	}

	if err := ccAdaptor.Initialize(); err != nil {
		logger.Errorf("error from Initialize %v", err)
		os.Exit(-1)
	}

	// ###### install #######
	if err := ccAdaptor.InstallCC(chainCodeID, chainCodePath, chainCodeVersion, nil); err != nil {
		logger.Errorf("installCC return error: %v", err)
	}

	// ###### instantiate #######
	if err := ccAdaptor.InstantiateCC(chainCodeID, ccAdaptor.ChainID, chainCodePath, chainCodeVersion, nil); err != nil {
		logger.Errorf("instantiateCC return error: %v", err)
	}

	// Define command-line flags that are valid for all peer commands and
	// subcommands.
	mainFlags := mainCmd.PersistentFlags()
	mainFlags.BoolVarP(&versionFlag, "version", "v", false, "Display current version of fabric peer server")
	mainCmd.AddCommand(VersionCmd())
	mainCmd.AddCommand(AppCmd())

	runtime.GOMAXPROCS(viper.GetInt("core.gomaxprocs"))

	// On failure Cobra prints the usage message and error string, so we only
	// need to exit with a non-0 status
	if mainCmd.Execute() != nil {
		os.Exit(1)
	}
	logger.Info("Exiting.....")
}
