package it.polito.tdp.borders.model;

public class Border {
	
	private Country s1;
	private Country s2;
	
	public Border(Country s1, Country s2) {
		super();
		this.s1 = s1;
		this.s2 = s2;
	}

	public Country getS1() {
		return s1;
	}

	public void setS1(Country s1) {
		this.s1 = s1;
	}

	public Country getS2() {
		return s2;
	}

	public void setS2(Country s2) {
		this.s2 = s2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((s1 == null) ? 0 : s1.hashCode());
		result = prime * result + ((s2 == null) ? 0 : s2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Border other = (Border) obj;
		if (s1 == null) {
			if (other.s1 != null)
				return false;
		} else if (!s1.equals(other.s1))
			return false;
		if (s2 == null) {
			if (other.s2 != null)
				return false;
		} else if (!s2.equals(other.s2))
			return false;
		return true;
	}

}
