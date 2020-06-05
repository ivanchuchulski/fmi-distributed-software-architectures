#!/bin/bash

function testIterations() {
    local output="iterations-550-five-runs.txt"
	local maxThreads=24
    local iterations=550
    local imageSize="1920x1440"
    
    echo "----------------------------------------" >> "${output}"
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "iterations : ${iterations}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    echo "thread : 1" >> "${output}"
    for run in $(seq 5)
    do 
        local options="-q -t 1 -s ${imageSize} -i ${iterations}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!
    done
    echo "" >> "${output}"

    for (( thread=2; thread<="${maxThreads}"; thread+=2 ))
    do
        echo "thread : ${thread}" >> "${output}"
        for run in $(seq 5)
        do 
            local options="-q -t ${thread} -s ${imageSize} -i ${iterations}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}

testIterations