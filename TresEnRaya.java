package edu.uclm.esi.disoft.dominio.tresenraya;

import java.io.IOException;
import java.util.Arrays;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;
import edu.uclm.esi.disoft.dao.tresenraya.DAOTresEnRaya;
import edu.uclm.esi.disoft.dao.Perdedores.DAOPerdedores;
import edu.uclm.esi.disoft.dao.ranking.DAORanking;
import edu.uclm.esi.disoft.dao.Ganadores.DAOGanadores;
import edu.uclm.esi.disoft.dominio.Manager;
import edu.uclm.esi.disoft.dominio.Movimiento;
import edu.uclm.esi.disoft.dominio.Partida;
import edu.uclm.esi.disoft.dominio.Usuario;


public class TresEnRaya extends Partida {
	static int contador = 0;
	private String[][] tablero=new String[3][3];
	private Usuario jugadorA;
	private Usuario jugadorB;
	private Usuario winer;
	private Usuario loser;
	
	public TresEnRaya(Usuario a, Usuario b) throws JSONException{
		super();
		this.jugadorA=a;
		this.jugadorB=b;
		this.addJugador(a);
		a.setFicha("X");
		this.addJugador(b);
		b.setFicha("O");
		this.jugadorConTurno=a;
		
	}

	@Override
	protected void setTurno() throws JSONException {
		if(this.jugadorConTurno.getNombre()==this.jugadorA.getNombre()){
			jugadorConTurno=this.jugadorB;
		}else{
			jugadorConTurno=this.jugadorA;
		}
		notificarMovimiento();
		notificarEspera();
		
	}

	@Override
	protected void actualizarTablero(Movimiento m) {
		
		String nombredeljugadorquemueve=m.getjugador();
		Usuario jugador=this.jugadores.get(nombredeljugadorquemueve);
		tablero[m.getfila()][m.getcolumna()]=jugador.getFicha();
		for(int i=0; i<3;i++){
			for(int j=0; j<3;j++){
				System.out.println(tablero[i][j]);
			}
		}
	}

	@Override
	protected void comprobarLegalidad(Movimiento m) throws Exception {
		String nombredeljugadorquemueve=m.getjugador();
		Usuario jugador=this.jugadores.get(nombredeljugadorquemueve);
		if(jugador==null) 
			throw new Exception("usted no esta jugando aqui, compañero/a");
		if(jugador!=this.jugadorConTurno)
			throw new Exception("no es tu turno, forastero");
		if(0>m.getfila() && m.getfila()>tablero.length && 0>m.getcolumna() && m.getcolumna()>tablero[0].length){
			throw new Exception("te has salido del tablero");
		}
		
		if(tablero[m.getfila()][m.getcolumna()]!=null){
				throw new Exception("esta casilla ya esta ocupada, prueba con otra");
		}
	}

	@Override
	public void insert() {
		DAOTresEnRaya.insert(this);
		
	}

	public Usuario getJugadorA() {
		return this.jugadorA;
	}
	public Usuario getJugadorB() {
		return this.jugadorB;
	}
	
	
	
	@Override
	public void notificarInicioPartida() throws JSONException {
		
		JSONObject jso=new JSONObject();
		jso.put("idpartida", this.idPartida);
		jso.put("jugador1", this.jugadorA.getNombre());
		jso.put("jugador2", this.jugadorB.getNombre());
		jso.put("jugadorconelturno", this.jugadorConTurno.getNombre());
		jso.put("juego", this.getClass().getSimpleName());
		jso.put("idPartida", this.idPartida);
		jso.put("tipo", "CrearPartida");
		System.out.println(jso.toString());
		
		this.jugadorA.enviarMensaje( jso);
		this.jugadorB.enviarMensaje( jso);
		
	}
	
	public void notificarMovimiento() throws JSONException{
		
		JSONObject jso=new JSONObject();
		jso.put("idpartida", this.idPartida);
		jso.put("jugador1", this.jugadorA.getNombre());
		jso.put("jugador2", this.jugadorB.getNombre());
		jso.put("jugadorconelturno", this.jugadorConTurno.getNombre());
		jso.put("juego", this.getClass().getSimpleName());
		jso.put("idPartida", this.idPartida);
		jso.put("tipo", "Movimiento");
		if(this.jugadorConTurno.getNombre()==this.jugadorA.getNombre()){
			this.jugadorA.enviarMensaje( jso);
		}else{
			if (this.jugadorConTurno.getNombre()==this.jugadorB.getNombre()){
				this.jugadorB.enviarMensaje( jso);
			}
		}	
		
	}

	public void notificarEspera() throws JSONException{
		
		JSONObject jso=new JSONObject();
		jso.put("idpartida", this.idPartida);
		jso.put("jugador1", this.jugadorA.getNombre());
		jso.put("jugador2", this.jugadorB.getNombre());
		jso.put("jugadorconelturno", this.jugadorConTurno.getNombre());
		jso.put("juego", this.getClass().getSimpleName());
		jso.put("idPartida", this.idPartida);
		jso.put("tipo", "Espera");
		if(this.jugadorConTurno.getNombre()!=this.jugadorA.getNombre()){
			this.jugadorA.enviarMensaje( jso);
		}else{
			if (this.jugadorConTurno.getNombre()!=this.jugadorB.getNombre()){
				this.jugadorB.enviarMensaje( jso);
			}
		}
	}
	
	public void notificarOponente() throws JSONException{
		JSONObject jso1=new JSONObject();
		JSONObject jso2=new JSONObject();
		jso1.put("oponente", this.jugadorA.getNombre());
		jso1.put("tipo", "contrincante");
		jso2.put("oponente", this.jugadorB.getNombre());
		jso2.put("tipo", "contrincante");
		this.jugadorA.enviarMensaje(jso2);
		this.jugadorB.enviarMensaje(jso1);
		
	}
	public void mandarTablero(Movimiento m) throws JSONException{
		JSONObject jso1= new JSONObject();
		JSONArray array = new JSONArray(Arrays.asList(tablero));
		jso1.put("tipo", "jugada");
		jso1.put("tablero", array);
		this.jugadorA.enviarMensaje(jso1);
		this.jugadorB.enviarMensaje(jso1);
	}

	@Override
	protected void comprobarGanador() throws JSONException {
		boolean ganador=false;
		JSONObject jso=new JSONObject();
		JSONObject jso1=new JSONObject();
		jso.put("jugadorconelturno", this.jugadorConTurno.getNombre());
		jso.put("tipo", "Ganador");
		jso1.put("jugadorconelturno", this.jugadorConTurno.getNombre());
		jso1.put("tipo", "Perdedor");
		for(int i=0; i<3;i++){
			if((tablero[i][0]==tablero[i][1] && tablero[i][1]==tablero[i][2]) && (tablero[i][0]!=null && tablero[i][1]!=null && tablero[i][2]!=null))
				ganador=true;
			if((tablero[0][i]==tablero[1][i] && tablero[1][i]==tablero[2][i]) && (tablero[0][i]!=null && tablero[1][i]!=null && tablero[2][i]!=null))
				ganador=true;
			
		}
		if((tablero[0][0]==tablero[1][1] && tablero[1][1]==tablero[2][2]) && (tablero[0][0])!=null && tablero[1][1]!=null && tablero[2][2]!=null )
			ganador=true;
		if((tablero[0][2]==tablero[1][1] && tablero[1][1]==tablero[2][0]) && (tablero[0][2])!=null && tablero[1][1]!=null && tablero[2][0]!=null )
			ganador=true;
		
		if(this.jugadorConTurno.getNombre()==this.jugadorA.getNombre() && ganador){
			for(int i=0; i<3;i++){
				for(int j=0; j<3;j++){
					tablero[i][j]=null;
				}
			}
			
			this.jugadorA.enviarMensaje(jso);
			this.jugadorB.enviarMensaje(jso1);
			winer=jugadorA;
			loser=jugadorB;
			DAOGanadores.insert(winer);
			DAOPerdedores.insert(loser);
			JSONObject json1= new JSONObject();
			json1.put("tipo", "FinDePartida");
			json1.put("texto", "algo");
			this.jugadorA.enviarMensaje(json1);
			this.jugadorB.enviarMensaje(json1);
			DAORanking.insert(this.jugadorA);
			DAORanking.insert(this.jugadorB);
			
			
		}else if(this.jugadorConTurno.getNombre()==this.jugadorB.getNombre() && ganador){
			for(int i=0; i<3;i++){
				for(int j=0; j<3;j++){
					tablero[i][j]=null;
				}
			}
			
			this.jugadorA.enviarMensaje(jso1);
			this.jugadorB.enviarMensaje(jso);
			winer=jugadorB;
			loser=jugadorA;
			DAOGanadores.insert(winer);
			DAOPerdedores.insert(loser);
			JSONObject json2= new JSONObject();
			json2.put("tipo", "FinDePartida");
			json2.put("texto", "algo");
			this.jugadorA.enviarMensaje(json2);
			this.jugadorB.enviarMensaje(json2);
			DAORanking.insert(this.jugadorA);
			DAORanking.insert(this.jugadorB);
			
		}
		
		
	}
	
	protected void comprobarEmpate() throws JSONException, IOException {
		
		JSONObject jso=new JSONObject();
		jso.put("jugadorconelturno", this.jugadorConTurno.getNombre());
		jso.put("tipo", "Empate");
		for(int i=0; i<3;i++){
			for(int j=0; j<3;j++){
				if(tablero[i][j]==null)
					return;
			}
		}
		for(int i=0; i<3;i++){
			for(int j=0; j<3;j++){
				tablero[i][j]=null;
			}
		}
		
		this.jugadorA.enviarMensaje(jso);
		this.jugadorB.enviarMensaje(jso);
		JSONObject json2= new JSONObject();
		json2.put("tipo", "FinDePartida");
		json2.put("texto", "algo");
		this.jugadorA.enviarMensaje(json2);
		this.jugadorB.enviarMensaje(json2);
	}

	@Override
	protected void comprobarMisil(Movimiento m) throws JSONException {/* aqui no hay misiles */}

	@Override
	protected void notificarBarco() throws JSONException {/*en esta clase no hace nada*/}

	
	
}
