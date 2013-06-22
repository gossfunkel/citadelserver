class Launcher
  
  require_relative 'Server'
  require_relative 'Game'
  
  STDOUT.sync = true
  
  server = Server::new
  print "server made"
  game = Game::new
  serverThread = Thread.new{server.start()}
  gameThread = Thread.new{game.start()}
  
  while (running)
    print "Stop? "
    input = gets.chomp
    if (input.equals?("yes"))
      running = false
    end
  end
  
  server.stop
  game.stop
  gameThread.join
  serverThread.join
  
end