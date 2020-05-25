#!/bin/bash

function testRectanle() {
    local output="rectangle-1-five-runs.txt"
	local maxThreads=20
    local rectangle="-2.0:0.0:-2.0:0.0"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "rectangle 1 : ${imageSize}" >> "${output}"

    for (( thread=1; thread<="${maxThreads}"; thread++ ))
    do
        echo "thread : ${thread}" >> "${output}"

        for run in $(seq 5)
        do 
            local options="-q -t ${thread} -r ${rectangle}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}

testRectanle