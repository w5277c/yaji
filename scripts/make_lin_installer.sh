source config.sh

echo Creating linsetup-$VERSION.zip
rm -rf lin
mkdir -p lin/install
mkdir -p lin/data/uninstall/
mkdir -p lin/data/$APPDIR/
cp -R jre_lin lin/data/jre
cp ../yaji/target/yaji.jar lin/install/yaji.jar
cp ../yaji/target/yaji.jar lin/data/uninstall/yaji.jar

cp $APPPATH/*.jar lin/data/$APPDIR/
cp run.sh lin/run.sh
chmod a+x lin/run.sh
cp uninstall.sh lin/data/uninstall/
chmod a+x lin/data/uninstall/uninstall.sh

cp icon.png lin/data/$APPDIR/
cp icon.png lin/data/uninstall/
cp launcher.sh lin/data/$APPDIR/
VAR=$APPCLASSNAME" \$@"
echo $VAR >> lin/data/$APPDIR/launcher.sh
chmod a+x lin/data/$APPDIR/launcher.sh

./makeself.sh lin linsetup_v$VERSION.sh "$APPDIR" ./run.sh


