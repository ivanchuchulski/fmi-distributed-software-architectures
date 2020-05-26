#!/bin/bash

function testIterations() {
    local output="iterations-350-five-runs.txt"
	local maxThreads=24
    local iterations=350
    local imageSize="1280x960"
	    
	truncate -s 0 "${output}"
    
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