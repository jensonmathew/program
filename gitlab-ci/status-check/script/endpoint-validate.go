package main

import (
    "fmt"
    "net/http"
    "log"
    "os"
)

func main() {
    resp, err := http.Get(os.Getenv("APP_URL") + os.Getenv("DOMAIN") + os.Getenv("PING_PATH"))
    if err != nil {
        log.Fatalf("Checking the Application response %v", err)
    }

    // Print the HTTP Status Code and Status Name
    fmt.Println("HTTP Response Status:", resp.StatusCode, http.StatusText(resp.StatusCode))

    if resp.StatusCode >= 200 && resp.StatusCode <= 299 {
        fmt.Println("HTTP status is in the 200 range. So we are good for the domain "   + os.Getenv("APP_URL") + os.Getenv("DOMAIN"))
    } else {
        fmt.Println("Connection Broken ! Please have a look at "   + os.Getenv("DOMAIN") +  " deployment status.")
        os.Exit(1)
    }
}
