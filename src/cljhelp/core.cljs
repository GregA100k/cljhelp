(ns cljhelp.core
  (:require [clojure.browser.dom :as dom]
 ))

(enable-console-print!)

(defn paste-value
 " help from https://gist.github.com/kohyama/6192253"
  [t]
  (let [el (dom/get-element "text")
        highlighted-value (aget t "data")
       ]
    (dom/set-text el highlighted-value)))
  
(defn current-tab
  "taken from https://github.com/marcelocra/chrome-extension-cljs-example/blob/master/src/chrome_extensions/background/events.cljs"
  [cb]
  (.query js/chrome.tabs
          #js {:active true :currentWindow true}
          (fn [tabs]
            (cb (first tabs)))))

(defn pasteSelection []
  (let [f1 (fn [tab] (.sendMessage js/chrome.tabs (aget tab "id") #js {:method "getSelection"} paste-value))
       ]
  (current-tab f1)))

(defn init []
  (pasteSelection)
)

(set! (.-onload js/window) init)
