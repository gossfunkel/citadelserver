class Server
  
  require 'socket'
  require 'Game'
  
  def initialize
    puts "Server starting up at #{Time.now}..."
  end
  
  def start
    $running = true
    $speech = Array.new
    $connectedPlayers = Array.new
    $socket = UDPSocket.new
    $socket.bind(nil, 1042)
    run
  end
  
  def run
    while($running)
      #text, sender = $socket.recvfrom(16)
    end
  end
  
  def exit
    $running = false
    puts "Server going down at #{Time.now}."
  end

  $server = Server.new
  $server.start 
  
end