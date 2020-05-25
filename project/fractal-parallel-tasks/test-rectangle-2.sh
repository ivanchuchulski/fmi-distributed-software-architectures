#!/bin/bash

function testRectanle() {
    local output="rectangle-1-five-runs.txt"
	local maxThreads=20
    # local rectangle1="-0.75:0.75:-1.0:1.0"
    local rectangle="1.5:2.0:-0.25:0.25"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "rectangle : ${imageSize}" >> "${output}"

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