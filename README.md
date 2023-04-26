# Hackberry

## About
A basic chess program in Java. I wanted to make something like this for a long time but kept putting it off.

First version (Khan Academy Processing.js): https://www.khanacademy.org/computer-programming/chess-program/5273424969646080

## Timeline
 - Around the end of December - Started project
 - Jan 4, 2023 - Successfully recreated the Opera Game (Paul Morphy vs Duke of Brunswick & Count Isouard, 1858).
 - Jan 8, 2023 - En passant, castling, checks, and promotion are recognized.
 - Jan 26, 2023 - Moved from 8x8 board search to piece HashMaps. ~500K -> ~65K iterations.
 - Feb 20, 2023 - AI makes random moves. This includes promotion, en passant, and castling.
 - Apr 9, 2023 - Took a break for other pursuits, organized BoardEval.java better and got things working again.
 - Apr 10, 2023 - AI is semi-working, but it's so slow it can only see 1 move in advance (even then it takes a few seconds).
 - Apr 11, 2023 - Purely materialistic AI can see 1-2 moves ahead, taking ~15 seconds per move.
 - Apr 17, 2023 - Switched from complicated HashMap representation to a simpler array-based move generation. Still similarly efficient.
 - Apr 19, 2023 - Added some basic minimax. Program seems to make good decisions.
 - Apr 22, 2023 - Experimented with negamax before switching back to minimax.
 - Apr 26, 2023 - AI runs correctly most of the time, taking ~20 seconds per move.

<br>

## Gallery
<p>January screenshot</p>
<img src="https://github.com/gyang0/Hackberry/blob/main/docImgs/Hackberry_ExampleImg.png" style="width:300px">
<p>April screenshot</p>
<img src="https://github.com/gyang0/Hackberry/blob/main/docImgs/Hackberry_ExampleImg_2.png" style="width:600px">
