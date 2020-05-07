awk -F"," '{ instructions += {}'+ $5; covered += $5 } END { print covered, "/", instructions, " instructions covered"; print 100*covered/instructions, "% covered"  }' target/jacoco/jacoco.csv'
