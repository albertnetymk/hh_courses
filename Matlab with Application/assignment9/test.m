FS=44100;
r=audiorecorder(FS, 16, 1);
record(r);
pause;
stop(r);
speech = getaudiodata(r);
save mytalk speech FS
% play back
clear all
load mytalk
sound(speech, FS)
