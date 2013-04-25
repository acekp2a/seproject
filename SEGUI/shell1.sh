cd /home/regulus/workspace/SEGUI/ezw/work/
cp -i $1 /home/regulus/workspace/SEGUI/ezw/work/
convert $2 -resize 256x256! $2
make image=$2 filter=haar maxbytes=$3
rm $2
