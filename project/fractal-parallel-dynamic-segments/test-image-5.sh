#!/bin/bash

function testImage() {
    local output="output-image-5-five-runs.txt"
	local maxThreads=24
    local imageSize="3200x2400"

    echo "----------------------------------------" >> "${output}"     
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    echo "thread : 1" >> "${output}"
    for run in $(seq 5)
    do 
        local options="-q -t 1 -s ${imageSize}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!
    done
    echo "" >> "${output}"

    for (( thread=2; thread<="${maxThreads}"; thread+=2 ))
    do
        echo "thread : ${thread}" >> "${output}"
        for run in $(seq 5)
        do 
            local options="-q -t ${thread} -s ${imageSize}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}

testImage
