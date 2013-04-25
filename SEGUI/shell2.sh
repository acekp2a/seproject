cd /home/regulus/workspace/SEGUI/ezw/work/
cp -i $1 /home/regulus/workspace/SEGUI/ezw/work/
convert $2 -resize 512x512! $2
make image=$2 filter=haar maxbytes=10000000
rm $2
