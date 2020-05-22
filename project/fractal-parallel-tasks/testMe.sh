#!/bin/bash

function testWithMultipleRuns() {    
    for thread in {1..5}
    do
        for run in {1..5}
        do
            echo "the stuff"
        done
    done
}


function testWithSingleRun() {
    output="output.txt"
	maxThreads=20
    imageSize="1280x960"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    for thread in $(seq "${maxThreads}")
    do
        options="-t ${thread} -s ${imageSize}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!

        echo "" >> "${output}"
    done
}


testWithSingleRun

