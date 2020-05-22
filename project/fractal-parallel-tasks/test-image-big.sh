function testImageBig() {
    local output="output-big-image-five-runs.txt"
	local maxThreads=20
    local imageSize="1280x960"
	    
	truncate -s 0 "${output}"
    
	date >> "${output}"
    echo "maxThreads : ${maxThreads}" >> "${output}"
    echo "imageSize : ${imageSize}" >> "${output}"

    for thread in $(seq "${maxThreads}")
    do
        echo "thread : ${thread}"

        for run in $(seq 5)
        do 
            local options="-q -t ${thread} -s ${imageSize}"
            bash ./runMe.sh "${options}" >> "${output}"
            wait $!
        done
        echo "" >> "${output}"
    done
}