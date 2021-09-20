(var git-prefix "https://phunanon.github.io/InsituxScripts/")
(load (str git-prefix "funcs/p-info.txt"))
(function git-load file
  (load (str git-prefix "chatbot/" file ".clj")))
(git-load "list.clj")
(git-load "flak.clj")
(git-load "cmds.clj")

;Replace with usage of (symbols) upon Insitux version 0918
(var commands ["list" "cmds" "flak"])
(var helps ["list" "flak"])

(function msg (.. dl.util.message args))
(function strf ((.. str args)))


(function on-message sender channel message
  ;Make parts[0] lowercase upon Insitux version 0913
  (let parts (split message))

  (when (= (0 parts) "help")
    (if (zero? (len parts))
      (msg "Use 'cmds' to see all commands then something like 'help heal'")
      (msg "Help for " (1 parts) ":\n"
           (strf "chatbot-help-" (1 parts)))))

  (let command (commands (0 parts)))
  (when command
    ((str "chatbot-" command) sender message parts channel)))

(dl.events.on_chat_message.kill)
(dl.events.on_chat_message.connect on-message)
(msg "Loaded Insitux Chatbot! Send 'cmds' to see list of commands.")