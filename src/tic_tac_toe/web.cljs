(ns tic-tac-toe.web
  (:require [clojure.browser.repl :as repl]
            [goog.events :as events]
            [goog.dom.classes :as gcl]
            [goog.dom :as gdom]
            [goog.dom.classes :as gstyle]
            [goog.array :as arr]
            [clojure.browser.dom :as dom]
            [tic-tac-toe.board :as brd]
            [tic-tac-toe.defensive :as defense]
            [clojure.string :as str]))
  

(def board-state (atom (brd/init-board)))

(defn- elt->move [e]
  (js/parseInt (last (.id e))))

(defn- record-move [move]
  (let [after-human-move (brd/move-player @board-state move :x)
        after-computer-move (brd/move-player 
                              after-human-move 
                              (defense/find-computer-move after-human-move :o) 
                              :o)]
    (reset! board-state after-computer-move)))

(defn- show-msg [& rest]
  (gdom/setTextContent 
    (dom/get-element "messages")
    (apply str rest)))

(defn- listen-for-click [elt]
  (events/listen
    elt
    "click"
    (create-move-listener elt)))

(defn- nth-cell [n]
  (dom/get-element (str "square" n)))

(defn- marked-cell [elt index value ws]
  (when (some #{index} ws)
    (gstyle/add elt "win"))
  (gdom/setTextContent elt (name value)))

(defn- render-board [b]
  (let [ws (brd/winning-seq @board-state)]
    (doseq [[index value] (brd/indexed-board b)]
      (let [elt (nth-cell index)]
        (events/removeAll elt "click")
        (if (and (nil? value) (brd/moves-remaining? @board-state))
          (listen-for-click elt)
          (marked-cell elt index value ws))))))

(defn- report-on-move [move]
  (let [winner (brd/winner @board-state)
        tie (brd/tie? @board-state)
        message (cond
                  winner (str (name winner) " has won the game.")
                  tie (str "The game has ended in a tie")
                  :else (str "Moved to " (inc move)))]
    (show-msg message)))

(defn- create-move-listener [elt]
  (let [move (elt->move elt)]
    #(do 
       (record-move move)
       (render-board @board-state)
       (report-on-move move))))

(defn start-app []    
  (render-board @board-state))

(start-app)

