#!/bin/bash

BASEDIR=$(dirname "$0")

#publicDir=$BASEDIR/Public
#outDir=$BASEDIR/myOut
#diffDir=$BASEDIR/myDiff

publicDir=/Users/khanhnguyen/MiniShogi/test/Public
outDir=/Users/khanhnguyen/MiniShogi/test/myOut
diffDir=/Users/khanhnguyen/MiniShogi/test/myDiff

cd $BASEDIR/../src && javac MiniShogi.java

# compile the application

total=0
pass=0

# run the application with correct arguments and check diff for expected outputs
for file in $publicDir/*.in
do
#    inputFileName=${file/..\/Public\//}
#    echo $inputFileName
#
#    fileName=${inputFileName/.in/}
#    echo $fileName

#    echo "print file--> "$file
    inputFileName=$(basename $file)
    fileName=${inputFileName/.in/}
#    echo "printing file "$fileName


    java MiniShogi -f $publicDir/"$fileName".in > $outDir/"$fileName".out

#    DIFF=$(diff $outDir/"$fileName".out $publicDir/"$fileName".out)
    DIFF=$(diff -u $outDir/"$fileName".out $publicDir/"$fileName".out)

    if [ "$DIFF" != "" ]
    then
        echo "> FAILED TEST " $publicDir/"$fileName".in "\n"
#        diff $outDir/"$fileName".out $publicDir/"$fileName".out > $diffDir/"$fileName".out
        diff -u $outDir/"$fileName".out $publicDir/"$fileName".out > $diffDir/"$fileName".out
    else
        echo "passed test " $publicDir/"$fileName".in "\n"
        pass=$((pass+1))
    fi

    total=$((total+1))

done

fail=$((total-pass))

echo "\nTOTAL " $total " \nPASSED " $pass " \nFAILED " $fail
