(ns cljhelp.core
  (:require [clojure.browser.dom :as dom]
 ))

(defn paste-value
 " help from https://gist.github.com/kohyama/6192253"
  [t]
  (let [spot (dom/get-element "spot")
        el (dom/element "textarea" {:cols 50 :rows 10})
        highlighted-value t
        message (str t " is not in the list of functions")
        set-the-value (dom/set-text el message)
       ]
        (dom/append spot el)
    ))

(defn redirect-to-cheatsheet [url] 
  (let [spot (dom/get-element "spot")
        ifr (dom/element "iframe" {:width "100%" :height "100%" :frameborder "0"})
        set-the-url (set! (.-src ifr) url)
       ]
    (dom/append spot ifr)
  ))

(def function-list [{:function-name "page" :url "https://clojure.org/api/cheatsheet"}])
(defn find-url [t]
  (first (filter #(= t (:function-name %)) function-list))
  )

(defn handle-highlight [t]
  (let [txt (aget t "data")
        function-url (find-url txt)
       ]
  (if (:function-name function-url) (redirect-to-cheatsheet (:url function-url))
    (paste-value txt))))
  
(defn current-tab
  "taken from https://github.com/marcelocra/chrome-extension-cljs-example/blob/master/src/chrome_extensions/background/events.cljs"
  [cb]
  (.query js/chrome.tabs
          #js {:active true :currentWindow true}
          (fn [tabs]
            (cb (first tabs)))))

(defn pasteSelection []
  (let [f1 (fn [tab] (.sendMessage js/chrome.tabs (aget tab "id") #js {:method "getSelection"} handle-highlight))
       ]
  (current-tab f1)))

(defn init []
  (pasteSelection)
)

(set! (.-onload js/window) init)
