(ns tic-tac-toe.web
  (:use [compojure.core :only [defroutes GET]]
        [ring.middleware.reload :only [wrap-reload]])
  (:require [ring.adapter.jetty :as ring]
            [hiccup.core :as h]
            [hiccup.page-helpers :as page]
            [compojure.route :as route]))

(def game-types
  {1 {:description "Human vs. Computer"}
   2 {:description "Computer vs. Human"}
   3 {:description "Computer vs. Computer"}
   })

(defn choose-game-type-page []
  (h/html 
    [:head
     [:title "Tic Tac Toe"]]
    (page/include-css "/stylesheets/base.css"
                      "/stylesheets/skeleton.css"
                      "/stylesheets/screen.css")
    [:body
     [:h1 "Tic Tac Toe"]
     [:h2 "Choose Game Type"]
     [:ol
      (map (fn [t] [:li (:description (val t))])  game-types)]]))

(defroutes 
  routes
  (GET "/" [] (choose-game-type-page))
  (route/resources "/"))

(def app
  (wrap-reload routes '(tic-tac-toe.web)))

(defn start []
  (ring/run-jetty app {:port 8080 :join? false}))

