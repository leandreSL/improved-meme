import java.util.ArrayList;

public class Registry implements Registry_itf {
	
	ArrayList<Person> persons = new ArrayList<Person>();
	
	@Override
	public void add(Person p) {
		persons.add(p);
	}

	@Override
	public String getPhone(String name) {
		for(Person p : persons) {
			if (p.name.equals(name)) {
				return p.phoneNumber;
			}
		}
		return null;
	}

	@Override
	public Iterable<Person> getAll() {
		return persons;
	}

	@Override
	public Person search(String name) {
		for(Person p : persons) {
			if (p.name.equals(name)) {
				return p;
			}
		}
		return null;
	}

}
