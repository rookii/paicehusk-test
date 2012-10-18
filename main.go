package main

import (
	"bufio"
	"bytes"
	"fmt"
	"github.com/Rookii/paicehusk"
	"io"
	"os"
	//"regexp"
)

func readFile(path string) (lines []string, err error) {
	var (
		file   *os.File
		part   []byte
		prefix bool
	)
	if file, err = os.Open(path); err != nil {
		return
	}
	defer file.Close()

	reader := bufio.NewReader(file)
	buffer := bytes.NewBuffer(make([]byte, 0))
	for {
		if part, prefix, err = reader.ReadLine(); err != nil {
			break
		}
		buffer.Write(part)
		if !prefix {
			lines = append(lines, buffer.String())
			buffer.Reset()
		}
	}
	if err == io.EOF {
		err = nil
	}
	return
}

func main() {
	out := bufio.NewWriter(os.Stdout)
	defer out.Flush()

	testFile, err := readFile("300TWL.txt")
	if err != nil {
		panic(err)
	}
	java, err := readFile("300TWL.txt.stm")
	if err != nil {
		panic(err)
	}

	//reg := regexp.MustCompile("[\\w]+")
	count := 0
	for i := range testFile {
		if paicehusk.DefaultRules.Stem(testFile[i]) != java[i] {
			out.WriteString(fmt.Sprintf("Line %v: %v |%v| |%v|\n",
				i+1, testFile[i], paicehusk.DefaultRules.Stem(testFile[i]), java[i]))
			count++
		}
	}
	out.WriteString(fmt.Sprintf("%v", count))
}
