#!/bin/bash

function testIterations() {
    local output="iterations-350-five-runs.txt"
	local maxThreads=20
    local iterations=350
    local imageSize="800x600"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "iterations : ${iterations}" >> "${output}"

    for (( thread=1; thread<="${maxThreads}"; thread++ ))
    do
        echo "thread : ${thread}" >> "${output}"

        for run in $(seq 5)
        do 
            local options="-q -t ${thread} -i ${iterations} -s ${imageSize}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}

testIterations