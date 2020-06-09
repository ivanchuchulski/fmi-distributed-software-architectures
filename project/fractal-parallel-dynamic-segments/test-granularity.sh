#!/bin/bash

function testGranularity() {
    local output="granularity-test.txt"
	local maxThreads=32
    local imageSize="1920x1440"
    local granularity=${1}

    echo "----------------------------------------" >> "${output}"
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "iterations : ${iterations}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    echo "thread : 1" >> "${output}"
    for run in $(seq 3)
    do 
        local options="-q -t 1 -s ${imageSize} -i ${iterations} -g ${granularity}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!
    done
    echo "" >> "${output}"

    for (( thread=2; thread<="${maxThreads}"; thread+=2 ))
    do
        echo "thread : ${thread}" >> "${output}"
        for run in $(seq 3)
        do 
            local options="-q -t ${thread} -s ${imageSize} -i ${iterations} -g ${granularity}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}

testGranularity 1
testGranularity 4
testGranularity 8
testGranularity 16
testGranularity 32
