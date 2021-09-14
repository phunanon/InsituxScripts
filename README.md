### About this repository

This repository hosts scripts written for the Roblox FPS game [Deadline](https://www.roblox.com/games/3837841034/0-19-2-Deadline) in the scripting language [Insitux](https://github.com/phunanon/Insitux).

### How to use

Open the console in a Deadline match (in a VIP server) by pressing \`  
Then copy-paste the `load` command provided with each script.  
If there are any problems or feedback join the [**Discord server**](https://discord.gg/w3Fc4YZ9Qw).

### [chatbot](https://phunanon.github.io/InsituxScripts/chatbot.txt)
`(load "https://phunanon.github.io/InsituxScripts/chatbot.txt")`  
Allows players to enter useful and fun commands in the chat such as for team switching, teleporting, causing explosions, killing with *brrr*...  
Optionally extend this chatbot by writing your own functions **after** you have loaded it:  
```clj
(function chatbot-extra sender channel message
  … do something …)
```

### (For Coders) Import Function
`(load "https://phunanon.github.io/InsituxScripts/funcs.txt")`  
Enables loading functions from the `func` directory of this repository as easy as:  
`(load-func "is-near?")`
