# R script for processing StructuredStochasticLogistic.xml output.

library(rjson)

df <- fromJSON(file='StructuredStochasticLogistic_output.json')

png('StructuredStochasticLogistic_mean.png', width=480, height=480)

plot(df$t, df$X$mean[[1]], 'l', col='blue', lwd=2,
     ylim=c(0,2000),
     xlab='Time',
     ylab='Deme population sizes',
     main='Population size dynamics')

lines(df$t, df$X$mean[[1]]-df$X$std[[1]], col='blue', lty=2)
lines(df$t, df$X$mean[[1]]+df$X$std[[1]], col='blue', lty=2)

lines(df$t, df$X$mean[[2]], col='purple', lwd=2)
lines(df$t, df$X$mean[[2]]-df$X$std[[2]], col='purple', lty=2)
lines(df$t, df$X$mean[[2]]+df$X$std[[2]], col='purple', lty=2)


legend('topleft', inset=.05,
       c(expression(E(N[1])), expression(E(N[2])), '+/- SD'),
       lty=c(1,1,2), lwd=c(2,2,1), col=c('blue','purple','black'))


dev.off()


png('StructuredStochasticLogistic_cov.png', width=480, height=480)

plot(df$t, df$X1X2$mean[[1]]/df$X$mean[[1]]/df$X$mean[[2]],
     xlab='Time',
     ylab=expression(Cov[rel](N[1],N[2])), 'l', lwd=2,
     main='Inter-deme covariance dynamics')

dev.off()
