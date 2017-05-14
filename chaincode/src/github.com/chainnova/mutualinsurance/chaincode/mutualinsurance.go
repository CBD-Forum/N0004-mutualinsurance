package main

import (
	"encoding/json"
	"fmt"
	"strconv"
	"strings"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

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

// InsuranceChaincode mutual insurance Chaincode implementation
type InsuranceChaincode struct {
}

// Init ...
func (t *InsuranceChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("########### insurance Init ###########")
	_, _ = stub.GetFunctionAndParameters()

	if transientMap, err := stub.GetTransient(); err == nil {
		if transientData, ok := transientMap["result"]; ok {
			fmt.Printf("Transient data in 'init' : %s\n", transientData)
			return shim.Success(transientData)
		}
	}
	return shim.Success(nil)
}

// Invoke ...
func (t *InsuranceChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("########### insurance Invoke ###########")
	function, args := stub.GetFunctionAndParameters()

	switch function {
	case "publishInsurance":
		return t.publishInsurance(stub, args)
	case "getInsurances":
		return t.getInsurances(stub, args)
	case "getInsurance":
		return t.getInsurance(stub, args)
	case "buyInsurance":
		return t.buyInsurance(stub, args)
	case "claimInsurance":
		return t.claimInsurance(stub, args)
	case "approveInsurance":
		return t.approveInsurance(stub, args)
	case "getClaim":
		return t.getClaim(stub, args)
	case "getProfile":
		return t.getProfile(stub, args)
	case "charge":
		return t.charge(stub, args)
	default:
		return shim.Error("Unknown function: " + function)
	}
}

// publishInsurance publishInsurance
func (t *InsuranceChaincode) publishInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("publishInsurance=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// TODO: check duplication
	txID := stub.GetTxID()

	body := []byte(args[0])
	var insurance Insurance
	err := json.Unmarshal(body, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.ID = txID
	fmt.Printf("publishInsurance: '%v'\n", insurance)

	attributes := []string{txID}
	key, err := stub.CreateCompositeKey("insurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(txID, []byte(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	insuranceByte, err := json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, insuranceByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

// getInsurances getInsurances
func (t *InsuranceChaincode) getInsurances(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getInsurances=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// get profile
	user := args[0]
	mapInsurances := make(map[string]bool, 0)
	attributes := []string{user}
	key, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	var profile Profile
	if value != nil {
		err := json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}
		for _, i := range profile.Insurances {
			mapInsurances[i.ID] = true
		}
	}

	// get insurances
	rqi, err := stub.GetStateByPartialCompositeKey("insurance", nil)
	if err != nil {
		return shim.Error(err.Error())
	}

	var insurances []Insurance
	for rqi.HasNext() {
		_, value, err := rqi.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var insurance Insurance
		err = json.Unmarshal(value, &insurance)
		if err != nil {
			return shim.Error(err.Error())
		}

		_, ok := mapInsurances[insurance.ID]
		if ok {
			insurance.Bought = true
		}

		// Claim
		claims := make([]Claim, 0)
		attributes := []string{insurance.ID}
		rqc, err := stub.GetStateByPartialCompositeKey("claimInsurance", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		for rqc.HasNext() {
			_, value, err := rqc.Next()
			if err != nil {
				return shim.Error(err.Error())
			}

			var claim Claim
			err = json.Unmarshal(value, &claim)
			if err != nil {
				return shim.Error(err.Error())
			}
			claims = append(claims, claim)
		}
		insurance.Claims = claims

		// statistics
		statistics := make([]InsuranceSta, 0)
		attributes = []string{insurance.ID}
		rqc, err = stub.GetStateByPartialCompositeKey("statistics", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		for rqc.HasNext() {
			_, value, err := rqc.Next()
			if err != nil {
				return shim.Error(err.Error())
			}

			var sta InsuranceSta
			err = json.Unmarshal(value, &sta)
			if err != nil {
				return shim.Error(err.Error())
			}
			statistics = append(statistics, sta)
		}
		insurance.Statistics = statistics

		insurances = append(insurances, insurance)
	}

	body, err := json.Marshal(insurances)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(body)
}

// getInsurance getInsurance
func (t *InsuranceChaincode) getInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getInsurance=%v\n", args)

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.")
	}

	// get profile
	mapInsurances := make(map[string]bool, 0)
	attributes := []string{args[0]}
	keyProfile, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(keyProfile)
	if err != nil {
		return shim.Error(err.Error())
	}

	var profile Profile
	if value != nil {
		err := json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}
		for _, i := range profile.Insurances {
			if i.ID == args[1] {
				mapInsurances[args[1]] = true
				break
			}
		}
	}

	// get insurance
	key, err := stub.GetState(args[1])
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	if value == nil {
		return shim.Success(nil)
	}

	var insurance Insurance
	err = json.Unmarshal(value, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	_, ok := mapInsurances[insurance.ID]
	if ok {
		insurance.Bought = true
	}

	// Claim
	claims := make([]Claim, 0)
	attributes = []string{insurance.ID}
	rqc, err := stub.GetStateByPartialCompositeKey("claimInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	for rqc.HasNext() {
		_, value, err := rqc.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var claim Claim
		err = json.Unmarshal(value, &claim)
		if err != nil {
			return shim.Error(err.Error())
		}
		claims = append(claims, claim)
	}
	insurance.Claims = claims

	// statistics
	statistics := make([]InsuranceSta, 0)
	attributes = []string{insurance.ID}
	rqc, err = stub.GetStateByPartialCompositeKey("statistics", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	for rqc.HasNext() {
		_, value, err := rqc.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var sta InsuranceSta
		err = json.Unmarshal(value, &sta)
		if err != nil {
			return shim.Error(err.Error())
		}
		statistics = append(statistics, sta)
	}
	insurance.Statistics = statistics

	// Serialize
	value, err = json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}

// buyInsurance buyInsurance
func (t *InsuranceChaincode) buyInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("buyInsurance=%v\n", args)

	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.")
	}

	txID := stub.GetTxID()

	user := args[0]
	body := []byte(args[1])
	var insuranceReq InsuranceReq
	err := json.Unmarshal(body, &insuranceReq)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("buyInsurance: '%v'\n", insuranceReq)

	attributes := []string{insuranceReq.IDInsurance, insuranceReq.IDUser, txID}
	key, err := stub.CreateCompositeKey("buyInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, body)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update insurance
	attributes = []string{insuranceReq.IDInsurance}
	key, err = stub.CreateCompositeKey("insurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Error("No such insurance")
	}
	var insurance Insurance
	err = json.Unmarshal(value, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	bought, err := strconv.ParseInt(insurance.CountBought, 10, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.CountBought = fmt.Sprintf("%v", bought+1)

	value, err = json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(string(key), value)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("buyInsurance: updated insurance\n")

	// update user profile for cache
	attributes = []string{insuranceReq.IDUser}
	key, err = stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	var profile Profile
	if value != nil {
		err := json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}
		// for _, i := range profile.Insurances {
		// 	if i.ID == insuranceReq.IDInsurance {
		// 		// TODO: update
		// 		return shim.Success(nil)
		// 	}
		// }
	}
	profile.ID = user
	amount, err := strconv.ParseFloat(insuranceReq.Amount, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	profile.Balance += amount
	profile.Insurances = append(profile.Insurances, insurance)
	value, err = json.Marshal(profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(string(key), value)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("buyInsurance: updated profile '%v'\n", profile)

	// update statistics
	var month string
	timeBought := insuranceReq.TimeBought
	s := strings.Split(timeBought, "-")
	if len(s) > 1 {
		month = s[0] + "-" + s[1]
	} else {
		location, _ := time.LoadLocation("Asia/Chongqing")
		month = time.Now().In(location).Format("2006-01")
	}
	attributes = []string{insuranceReq.IDInsurance, month}
	key, err = stub.CreateCompositeKey("statistics", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	var statistics InsuranceSta
	if value != nil {
		err := json.Unmarshal(value, &statistics)
		if err != nil {
			return shim.Error(err.Error())
		}
	}
	statistics.Month = month
	statistics.CountNew++
	value, err = json.Marshal(statistics)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	fmt.Printf("buyInsurance: updated statistics '%v'\n", statistics)

	return shim.Success(nil)
}

// claimInsurance claimInsurance
func (t *InsuranceChaincode) claimInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("claimInsurance=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// TODO: check duplication
	txID := stub.GetTxID()

	body := []byte(args[0])
	var claim Claim
	err := json.Unmarshal(body, &claim)
	if err != nil {
		return shim.Error(err.Error())
	}
	claim.IDClaim = txID
	claim.Status = "claimed"
	fmt.Printf("claimInsurance: '%v'\n", claim)

	// check bought
	attributes := []string{claim.IDInsurance, claim.IDUser}
	rqi, err := stub.GetStateByPartialCompositeKey("buyInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	} else if !rqi.HasNext() {
		return shim.Error("Have NOT bought")
	}

	// claim
	attributes = []string{claim.IDInsurance, claim.IDUser, txID}
	key, err := stub.CreateCompositeKey("claimInsurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(txID, []byte(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	claimByte, err := json.Marshal(claim)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(key, claimByte)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(nil)
}

// getClaim getClaim
func (t *InsuranceChaincode) getClaim(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getClaim=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	key, err := stub.GetState(args[0])
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}

// approveInsurance approveInsurance
func (t *InsuranceChaincode) approveInsurance(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("approveInsurance=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	// get claim
	body := []byte(args[0])
	var claimNew Claim
	err := json.Unmarshal(body, &claimNew)
	if err != nil {
		return shim.Error(err.Error())
	}

	keyByte, err := stub.GetState(claimNew.IDClaim)
	if err != nil {
		return shim.Error(err.Error())
	}

	var claim Claim
	claimByte, err := stub.GetState(string(keyByte))
	if err != nil {
		return shim.Error(err.Error())
	}
	if claimByte == nil {
		return shim.Error("No such insurance")
	}
	err = json.Unmarshal(claimByte, &claim)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update insurance
	attributes := []string{claim.IDInsurance}
	key, err := stub.CreateCompositeKey("insurance", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	value, err := stub.GetState(string(key))
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Error("No such insurance")
	}
	var insurance Insurance
	err = json.Unmarshal(value, &insurance)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.CountHelped++
	amountMax, err := strconv.ParseFloat(insurance.AmountMax, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	insurance.Amount += float64(amountMax)

	// update user profile: bought fee
	bought, err := strconv.ParseInt(insurance.CountBought, 10, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	feeAverage, err := strconv.ParseFloat(insurance.Fee, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	fee := amountMax/float64(bought) + feeAverage
	countQuit := 0
	attributes = []string{}
	rqc, err := stub.GetStateByPartialCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	for rqc.HasNext() {
		keyProfile, value, err := rqc.Next()
		if err != nil {
			return shim.Error(err.Error())
		}

		var profile Profile
		err = json.Unmarshal(value, &profile)
		if err != nil {
			return shim.Error(err.Error())
		}

		// distribute claims
		if profile.ID == claim.IDUser {
			// profile.Balance += claim.Amount
		}
		if profile.Balance > 0 && profile.Balance < fee {
			profile.Balance = 0
			balance, err := strconv.ParseFloat(insurance.Balance, 64)
			if err != nil {
				return shim.Error(err.Error())
			}
			balance -= fee - profile.Balance
			insurance.Balance = fmt.Sprintf("%v", balance)

			bought--
			insurance.CountBought = fmt.Sprintf("%v", bought)
			countQuit++
		} else {
			profile.CountHelped++
			profile.Fee += fee
		}

		value, err = json.Marshal(profile)
		if err != nil {
			return shim.Error(err.Error())
		}

		err = stub.PutState(keyProfile, value)
		if err != nil {
			return shim.Error(err.Error())
		}
	}

	value, err = json.Marshal(insurance)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(string(key), value)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update statistics
	var month string
	timeApproved := claimNew.TimeApproved
	s := strings.Split(timeApproved, "-")
	if len(s) > 1 {
		month = s[0] + "-" + s[1]
	} else {
		location, _ := time.LoadLocation("Asia/Chongqing")
		month = time.Now().In(location).Format("2006-01")
	}
	attributes = []string{insurance.ID, month}
	key, err = stub.CreateCompositeKey("statistics", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err = stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}

	var statistics InsuranceSta
	if value != nil {
		err := json.Unmarshal(value, &statistics)
		if err != nil {
			return shim.Error(err.Error())
		}
	}
	statistics.Month = month
	statistics.CountHelped++

	statistics.Amount += amountMax
	statistics.Cost += feeAverage * float64(bought)
	statistics.Fee = fee
	statistics.CountQuit += countQuit
	value, err = json.Marshal(statistics)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	// update claim
	claim.Amount = float64(amountMax)
	claim.Status = claimNew.Status
	claim.TimeApproved = claimNew.TimeApproved
	claim.Receipt = claimNew.Receipt
	claim.ReportThird = claimNew.ReportThird
	claim.ReportVisitor = claimNew.ReportVisitor

	claimByte, err = json.Marshal(claim)
	if err != nil {
		return shim.Error(err.Error())
	}
	err = stub.PutState(string(keyByte), claimByte)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Printf("approveInsurance: %v='%v'\n", keyByte, claim)

	return shim.Success(nil)
}

// getProfile getProfile
func (t *InsuranceChaincode) getProfile(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("getProfile=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	user := args[0]
	fmt.Printf("getProfile: user=%v\n", user)

	attributes := []string{user}
	keyProfile, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}

	value, err := stub.GetState(keyProfile)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Success(nil)
	}

	// update insurances according to insurance id
	var profile Profile
	err = json.Unmarshal(value, &profile)
	if err != nil {
		return shim.Error(err.Error())
	}
	var insurances []Insurance
	for _, i := range profile.Insurances {
		attributes = []string{i.ID}
		key, err := stub.CreateCompositeKey("insurance", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		value, err = stub.GetState(key)
		if err != nil {
			return shim.Error(err.Error())
		}
		var insurance Insurance
		err = json.Unmarshal(value, &insurance)
		if err != nil {
			return shim.Error(err.Error())
		}
		insurance.Bought = true
		// Claim
		claims := make([]Claim, 0)
		attributes := []string{insurance.ID}
		rqc, err := stub.GetStateByPartialCompositeKey("claimInsurance", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		for rqc.HasNext() {
			_, value, err := rqc.Next()
			if err != nil {
				return shim.Error(err.Error())
			}

			var claim Claim
			err = json.Unmarshal(value, &claim)
			if err != nil {
				return shim.Error(err.Error())
			}
			claims = append(claims, claim)
		}
		insurance.Claims = claims

		// statistics
		statistics := make([]InsuranceSta, 0)
		attributes = []string{insurance.ID}
		rqc, err = stub.GetStateByPartialCompositeKey("statistics", attributes)
		if err != nil {
			return shim.Error(err.Error())
		}
		for rqc.HasNext() {
			_, value, err := rqc.Next()
			if err != nil {
				return shim.Error(err.Error())
			}

			var sta InsuranceSta
			err = json.Unmarshal(value, &sta)
			if err != nil {
				return shim.Error(err.Error())
			}
			statistics = append(statistics, sta)
		}
		insurance.Statistics = statistics

		insurances = append(insurances, insurance)
	}
	profile.Insurances = insurances
	value, err = json.Marshal(profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(keyProfile, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}

func (t *InsuranceChaincode) charge(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Printf("charge=%v\n", args)

	if len(args) != 1 {
		return shim.Error("Incorrect number of arguments.")
	}

	body := []byte(args[0])
	var charge Charge
	err := json.Unmarshal(body, &charge)
	if err != nil {
		return shim.Error(err.Error())
	}

	attributes := []string{charge.IDUser}
	key, err := stub.CreateCompositeKey("profile", attributes)
	if err != nil {
		return shim.Error(err.Error())
	}
	value, err := stub.GetState(key)
	if err != nil {
		return shim.Error(err.Error())
	}
	if value == nil {
		return shim.Success(nil)
	}
	// update profile balance according to id
	var profile Profile
	err = json.Unmarshal(value, &profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	amount, err := strconv.ParseFloat(charge.Amount, 64)
	if err != nil {
		return shim.Error(err.Error())
	}
	profile.Balance += amount
	value, err = json.Marshal(profile)
	if err != nil {
		return shim.Error(err.Error())
	}

	err = stub.PutState(key, value)
	if err != nil {
		return shim.Error(err.Error())
	}

	return shim.Success(value)
}

func main() {
	err := shim.Start(new(InsuranceChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
