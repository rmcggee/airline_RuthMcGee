package dmacc.controller;

import java.io.Console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import dmacc.beans.Customer;
import dmacc.beans.CustomerReservation;
import dmacc.beans.Flight;
import dmacc.repository.CustomerRepository;
import dmacc.repository.CustomerReservationRepository;
import dmacc.repository.FlightRepository;

@Controller
public class CustomerReservationController {

	@Autowired
	CustomerReservationRepository crRepo;
	@Autowired
	CustomerRepository custRepo;
	@Autowired
	FlightRepository flightRepo;
	
	@GetMapping("/reservation")
	public String viewAllReservations(Model model) {
		if(crRepo.findAll().isEmpty()) {
			return addNewReservation(model);
		}
		model.addAttribute("reservations", crRepo.findAll());
		return "reservation";
	}
	
	@GetMapping("/inputRes")
	public String addNewReservation(Model model) {
		CustomerReservation cres = new CustomerReservation();
		model.addAttribute("newReservation", cres);
		model.addAttribute("flights", flightRepo.findAll());
		model.addAttribute("customers", custRepo.findAll());
		return "inputRes";
	}
	
	@PostMapping("/inputReservation")
	public String addReservation(Model model, @RequestParam String custId, @RequestParam String flightId, @RequestParam String seatId) {
		long tempCustId = Long.parseLong(custId);
		long tempFlightId = Long.parseLong(flightId);
		Customer myCust = custRepo.findById(tempCustId).orElse(null);
		Flight myFlight = flightRepo.findById(tempFlightId).orElse(null);
		CustomerReservation cres = new CustomerReservation(seatId, myFlight, myCust );
		crRepo.save(cres);
		return viewAllReservations(model);
	}

	@GetMapping("/editReservation/{id}")
	public String showUpdateReservation(@PathVariable("id") long id, Model model) {
		CustomerReservation cres = new CustomerReservation();
		model.addAttribute("newReservation", cres);
		return "inputRes";
	}
	
	@PostMapping("/updateReservation/{id}")
	public String reviseReservation(CustomerReservation cres, Model model) {
		crRepo.save(cres);
		return viewAllReservations(model);
	}
	
	@GetMapping("/deleteReservation/{id}")
	public String deleteCustomerReservation(@PathVariable("id") long id, Model model) {
		CustomerReservation cres = crRepo.findById(id).orElse(null);
		crRepo.delete(cres);
		return viewAllReservations(model);
	}

	
}
