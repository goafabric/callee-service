setcpm(120/4)

$: n("<0 4 0 9 7>*16")
.add("<7__6 5__6>*2")
.scale("g:minor").trans(-12)
.s("sawtooth")._pianoroll()

$: n("<0>*16").scale("g:minor").trans(-24)
.detune(rand)
.s("supersaw")._pianoroll()

$: s("bd:2!4").duck("3:4:5:6").duckdepth(.8).duckattack(16)._scope()