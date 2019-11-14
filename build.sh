echo build project..
yarn run build
echo copy file to android assets..
rm -rf /Users/rae/git/android-cnblogs-gitlab/module-resource/assets/*
cp -rf dist/* /Users/rae/git/android-cnblogs-gitlab/module-resource/assets/
echo OK
