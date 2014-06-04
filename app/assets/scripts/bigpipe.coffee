injectPipe = (pipe) ->
  if pipe.styles.length
    head.load pipe.styles, ->
      placePipe(pipe.html, pipe.id)
      if pipe.scripts.length
        head.load pipe.scripts
  else
    placePipe(pipe.html, pipe.id)

placePipe = (html, id) ->
  document.getElementById(id)?.innerHTML = html

BigPipe = window['BigPipe'] || {}
BigPipe.inject = injectPipe
window['BigPipe'] = BigPipe
