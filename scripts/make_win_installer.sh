source config.sh

echo winsetup-$VERSION.zip
rm -rf win
mkdir -p win/install
mkdir -p win/data/uninstall/
mkdir -p win/data/$APPDIR/
cp -R jre_win win/data/jre
cp ../yaji/target/yaji.jar win/install/yaji.jar
cp ../yaji/target/yaji.jar win/data/uninstall/yaji.jar
cp mslinks.jar win/install/
cp mslinks.jar win/data/uninstall/

cp $APPPATH/*.jar win/data/$APPDIR/
cp uninstall.cfg win/data/uninstall/exec.cfg
cp launcher.exe win/data/uninstall/uninstaller.exe

cp icon.ico win/data/$ADDDIR/
cp icon.ico win/data/uninstall/

cp exec.cfg win/data/$APPDIR/
echo $APPCLASSNAME >> win/data/$APPDIR/exec.cfg
cp launcher.exe win/data/$APPDIR/
cp launcher.cmd win/data/$APPDIR/
echo $APPCLASSNAME >> win/data/$APPDIR/launcher.cmd

( cd win
  rar a -sfx../windows.sfx ../winsetup_v$VERSION.exe *
  rar c ../winsetup_v$VERSION.exe < ../sfx.ini
)
zip winsetup_v$VERSION.zip winsetup_v$VERSION.exe
rm -f winsetup_v$VERSION.exe
