import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class WorkingTread extends Thread{
	private Socket client;
	private PrintWriter pw;
	private BufferedReader br;
	private LinkedList<Socket> users;
	
	public WorkingTread(Socket s, LinkedList<Socket> users) {
		client = s;
		this.users = users;
		try {
			pw = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), "UTF8"));
			br = new BufferedReader(new InputStreamReader(client.getInputStream(),"UTF8"));
			synchronized (users) {
				users.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		String s = null;
		System.out.println("Access: " + client.getInetAddress());
		printMain();
		while(true) {
			pw.println(">>");
			pw.flush();
			try {
				s = br.readLine();
				if(s.equals("����") || s.equals("��������"))
					break;
				else if(s.equals("����������") || s.equals("������"))
					checkConcurrentUsers();
				else
					pw.println("�������� ���ϴ� ��ɾ� �Դϴ�!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			client.close();
			System.out.println("Client closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkConcurrentUsers() {
		int n;
		synchronized (users) {
			n = users.size();
			pw.println("--���� ê�� ������ ��: " + n + "��");
			for(Socket s: users)
				pw.println("----ip: " + s.getInetAddress());
			pw.flush();
		}
	}

	private void printMain() {
		pw.println("--��ȭ�� �󿵽ð�ǥ ���� ê���� �����ϽŰ� ȯ���մϴ�!");
		pw.println("----��ɾ �Է��� �ּ���.");
		pw.flush();
	}
}
