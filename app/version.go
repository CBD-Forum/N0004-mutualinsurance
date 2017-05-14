package main

import (
	"fmt"

	"github.com/spf13/cobra"
	"github.com/spf13/viper"
)

// VersionCmd returns the Cobra Command for Version
func VersionCmd() *cobra.Command {
	return cobraCommand
}

var cobraCommand = &cobra.Command{
	Use:   "version",
	Short: "Print fabric app version.",
	Long:  `Print current version of fabric app server.`,
	Run: func(cmd *cobra.Command, args []string) {
		VersionPrint()
	},
}

// VersionPrint outputs the current executable version to stdout
func VersionPrint() {
	version := viper.GetString("app.version")
	fmt.Printf("Fabric app server version %s\n", version)
}
