
(function chatbot-list sender
  (let team    (p* sender :team))
  (let other   (if (= team "insurgents") "security" "insurgents"))
  (let players (dl.list_players))
  (let enemies (filter [other] players)))

(function chatbot-help-list
  "Not written yet.")
