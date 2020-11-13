#!/usr/bin/env bash
sudo gem install fir-cli

echo "FIR_TOKEN" $FIR_TOKEN
fir login $FIR_TOKEN
fir me

echo "APPCENTER_OUTPUT_DIRECTORY" $APPCENTER_OUTPUT_DIRECTORY"/"$APP_FILE 
fir p $APPCENTER_OUTPUT_DIRECTORY/$APP_FILE --oversea_turbo --dingtalk-access-token=$DINGTALK_ACCESS_TOKEN --dingtalk-custom-message=$DINGTALK_CUSTOM_MESSAGE
