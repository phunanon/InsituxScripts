(var cmds "Examples: \"heal XxPlayerxX\" or \"heal Xx\"
  \"players\" - list players on each team
  \"team security\" or \"team insurgent\" - switch to either team
  \"heal\" or \"heal [player]\" - heal you or a player fully
  \"drone\" or \"drone [player]\" - drone strike on your or player's position
  \"brrr [player]\" - airstrike on player
  \"flare\" or \"flare [player]\" - fire a flare above yourself or another player
  \"boom [player]\" - kill player with an explosion
  \"boom near [player]\" - cause an explosion near the player (non-lethal)
  \"tp [player] here\" or \"tp to [player]\" or \"tp [player1] to [player2]\" - teleport
  \"plant\" - plant claymore where you stand")

(function rand-circle-point position radius
  (-> (rand (* 2 PI))
     #[(cos %) 0 (sin %)]
      (map (* radius))
     @(map + position)))

(function ->name name
  (find #(starts? (lower-case name) (lower-case %))
        (dl.list_players)))

;e.g. (plr "Insitux" :pos [0 0 0])
;     (plr "ins" :pos) -> [0 0 0]
(function plr name key val
  (let prop (match key :pos "position" :team "team" :hp "health"))
  (catch ;in case the player is dead or something else is wrong
    (if val
      ((str "$dl.players." (->name name) prop) val)
      (prop (str "dl.players." (->name name))))
    :successful
    (print errors)))

(function alive? who
  ((str "dl.players." who ".is_alive")))

(var monologue
  @(map #(do (dl.util.message %) (wait 1))))


(function teleport from to
  (and (plr from :pos (plr to :pos))
       (str from " teleported to " to)))

(function heal who
  (and (plr who :hp 100)
       (str "Healed " who)))

(function boom who is-near
  (-> (plr who :pos)
     @(if is-near (rand-circle-point pos 48))
     #(dl.util.explosion % "TestGrenade"))
  (str "Exploded " (if is-near "near " "") who))

(function flare who
  (-> (plr who :pos)
     @(map + [0 100 0])
     #(dl.util.explosion % "TestGrenade"))
  (str "Fired flare above " who))

(function brrr who
  (if! (alive? who) (return))
  (monologue [
    (str "Airstrike target locked: " who)
    "Coming in hot in 5" "4" "3" "2..."])
  (wait 1)
  (let pos (plr who :pos))
  (let i 0)
  (while (< i 100)
    (let! i inc)
    (wait .01)
    (-> [(rand 10) 0 (rand 10)]
       #(dl.util.explosion (map + [(- (* i 2) 100) 0 0] % pos))))
  (str who " has been brrr'd."))

(function drone who
  (if! (alive? who) (return))
  (let pos (plr who :pos))
  (monologue [
    "Position locked!" "Drone strike in 4" "3" "2..."])
  (wait 1)
  ;Replace with `range` once fixed in Deadline
  (let i 0 lim (rand 5 10))
  (while (< i lim)
    (let! i inc)
    (wait .25)
    (-> [(rand -50 50) 30 (rand -50 50)]
       #(dl.util.explosion (map + % pos))))
  "Drone strike complete.")

(function command sender msg
  (let parts (split (lower-case msg))
       [_ b c d] parts)
  (match parts
    ["cmds"]          cmds
    ["players"]       (join (dl.list_players))
    ["heal"]          (heal sender)
    ["heal" _]        (heal b)
    ["team"]          (str sender " is in team " (plr sender :team))
    ["team" _]        (and (plr sender :team b) (str sender " is in team " b))
    ["tp" _ "here"]   (teleport b sender)
    ["tp" _ "to" _]   (teleport b d)
    ["tp" "to" _]     (teleport sender c)
    ["boom" _]        (boom b false)
    ["boom" "near" _] (boom b true)
    ["flare"]         (flare sender)
    ["flare" _]       (flare b)
    ["brrr" _]        (brrr b)
    ["drone"]         (drone sender)
    ["drone" _]       (drone b)
    :not-a-command))

(function on-message sender _ msg
  (let result (command sender msg))
  (if (!= result :not-a-command)
    (dl.util.fmessage
      (if (starts? "(" msg)
        (catch (eval msg) errors)
        (or result "There was a problem. Maybe the player isn't alive.")))))

(dl.events.on_chat_message.kill)
(dl.events.on_chat_message.connect on-message)
(dl.util.fmessage "Loaded Insitux chatbot! Chat \"cmds\" to see list of available commands.
Note: this is a CHAT bot, you send the commands in the chat.")
