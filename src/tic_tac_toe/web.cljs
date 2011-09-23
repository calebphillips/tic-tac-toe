(ns tic-tac-toe.web
  (:require [clojure.browser.repl :as repl]
            [goog.events :as events]
            [goog.dom.classes :as gcl]
            [goog.dom :as gdom]
            [goog.array :as arr]
            [clojure.browser.dom :as dom]
            [tic-tac-toe.board :as brd]
            [tic-tac-toe.defensive :as defense]
            [clojure.string :as str]))
  

(def board-state (atom (brd/init-board)))

(defn dump-board [board]
  (str/join "," (map (fn [s] (if s (name s) "EMPTY")) board)))

(defn click->move [e]
  (js/parseInt (last (.id e))))

(defn record-move [move]
  (let [after-human-move 
        (brd/move-player @board-state move :x)
        after-computer-move 
        (brd/move-player after-human-move 
                         (defense/find-computer-move after-human-move :o) 
                         :o)]
    (reset! board-state after-computer-move)))

(defn display-message [s]
  (gdom/setTextContent 
    (dom/get-element "messages")
    s))

(defn listen-for-click [e]
  (events/listen
    e
    "click"
    (create-move-listener e)))

(defn render-board [b]
  (doseq [[index value] (brd/indexed-board b)]
    (let [e (dom/get-element (str "square" index))]
      (do 
        ; A little weird here, removing and re-adding if the cell is empty
        (events/removeAll e "click")
        (if (nil? value)
          (listen-for-click e)
          (gdom/setTextContent e (name value)))))))

(defn create-move-listener [e]
  #(do 
     (record-move (click->move e))
     (render-board @board-state)
     (display-message (str "Moved to " (.id e)))))

(defn start-app []    
  (render-board @board-state))

(start-app)

