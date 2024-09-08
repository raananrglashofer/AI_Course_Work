package main

import (
	"fmt"
	"os"
)

func main() {
	argsWithProg := os.Args
	if len(argsWithProg) > 1 {
		arg := os.Args[1]
		fmt.Println("Hello " + arg)
	} else{
		fmt.Println("Hello ???")
	}
}
