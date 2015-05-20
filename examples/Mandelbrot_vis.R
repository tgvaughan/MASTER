library(rjson)

df <- fromJSON(file='Mandelbrot_output.json')

m <- list()
for (t in 1:length(df$t)) {
    m[[t]] <- matrix(nrow=100, ncol=50)
    for (i in 1:100) {
        for (j in 1:50) {
            m[[t]][i,j] <- df$X[[i]][[j]][t]
        }
    }
}

pdf('Mandelbrot_output.pdf', width=7, height=7)

image(-2 + 2.5*(0:100)/100, -1.2 + 2.4*(0:50)/50, m[[length(df$t)]],
      col=terrain.colors(10),
      xlab='Real', ylab='Imaginary',
      main='Diffused Mandelbrot set')

dev.off()
