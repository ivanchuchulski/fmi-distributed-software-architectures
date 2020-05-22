#!/bin/bash

function testImageSmall() {
    local output="output-small-image.txt"
	local maxThreads=20
    local imageSize="640x480"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    for thread in $(seq "${maxThreads}")
    do
        local options="-q -t ${thread} -s ${imageSize}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!

        echo "" >> "${output}"
    done
}

function testImageBig() {
    local output="output-big-image.txt"
	local maxThreads=20
    local imageSize="1280x960"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    for thread in $(seq "${maxThreads}")
    do
        local options="-q -t ${thread} -s ${imageSize}"
        bash ./runMe.sh "${options}" >> "${output}"
        wait $!

        echo "" >> "${output}"
    done
}

testImageSmall

testImageBig
