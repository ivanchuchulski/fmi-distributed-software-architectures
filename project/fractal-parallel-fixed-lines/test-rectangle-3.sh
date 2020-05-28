#!/bin/bash

function testRectangle() {
    local output="rectangle-3-five-runs.txt"
	local maxThreads=24
    local rectangle="-0.75:0.75:0.0:1.25"
    local imageSize="1920x1440"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "rectangle 3 : ${rectangle}" >> "${output}"
    echo "image size : ${imageSize}" >> "${output}"

    echo "thread : 1" >> "${output}"
    for run in $(seq 5)
    do 
        local options="-q -t 1 -r ${rectangle} -s ${imageSize}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!
    done
    echo "" >> "${output}"

    for (( thread=2; thread<="${maxThreads}"; thread+=2 ))
    do
        echo "thread : ${thread}" >> "${output}"

        for run in $(seq 5)
        do 
            local options="-q -t ${thread} -r ${rectangle} -s ${imageSize}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}

testRectangle