class Launcher

  #puts "File saved at #{File.mtime($0)}"
  
  require_relative 'server'
  require_relative 'game'
  
  STDOUT.sync = true
  
  def parseInput( input )
    puts "parsing"
    commands = [
      "exit", "say", "disconnect", "teleport", "location"
      ]
    if input.contains? commands[0]
      puts "exiting"
      $running = false
    end
    if input.contains? commands[1]
      puts input.substring(4)
    end
    if input.contains? commands[2]
      server.remove input.substring(11)
    end
    if input.contains? commands[3]
      server.tp input.substring(9)
    end
    if input.contains? commands[4]
      puts server.getPlayer(input.substring(11)).location
    end
    unless input.contains? commands.each
      puts "Command not recognised"
    end
  end
  
  #puts "Launcher started"
  server = Server.new
  game = Game.new
  serverThread = Thread.new{server.start()}
  gameThread = Thread.new{game.start()}
  running = true
  
  while (running)
    print ">> "
    input = gets
    if input.respond_to? :include?
      parseInput( input )
    else
      raise ArgumentError,
        "Input error: #{ input.class }"
    end
    puts input.class
    puts "String".class
  end

  game.stop
  server.stop
  gameThread.join
  serverThread.join
  
end
