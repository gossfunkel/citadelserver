class Server
  
  require 'socket'
  
  def initialize
    puts "Server starting up at #{Time.now}..."
    $game = Game.new
    $gameThread = Thread.new{game.start()}
    $serverThread = Thread.new{server.start()}
  end
  
  def start
    $running = true
    $speech = Array.new
    $connectedPlayers = Array.new
    socket = UDPSocket.new
    socket.bind(nil, 1042)
    run
  end
  
  def run
    while($running)
      text, sender = s.recvfrom(16)
    end
  end
  
  def exit
    $running = false
    puts "Server going down at #{Time.now}."
    $gameThread.join
    $serverThread.join
  end

  $server = Server.new
  $server.start 
  
end