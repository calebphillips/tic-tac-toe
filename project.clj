(defproject tic-tac-toe "1.0.0"
  :description "Text interfaced Tic Tac Toe game"
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [ring/ring-jetty-adapter "0.3.10"]
                 [compojure "0.6.3"]
                 [hiccup "0.3.6"]
                 [ring/ring-devel "0.3.10"]]
  :dev-dependencies [[speclj "1.2.0"] ]
  :test-path "spec/"
  :main tic-tac-toe.text-ui)
