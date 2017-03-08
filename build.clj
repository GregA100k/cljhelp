(require 'cljs.build.api)

(cljs.build.api/build "src" {:output-to "out/popup.js"})
