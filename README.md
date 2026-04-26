# Placement Readiness Analytics System
A Java-based MVP project that helps students track assessment performance and evaluate their placement readiness using analytics and smart suggestions.

## Overview

This application allows students to:
- Register and log in
- Add assessment scores (Coding, Aptitude, Interview)
- Track performance over time
- Analyze strengths and weaknesses
- Get placement readiness status with feedback

## Tech Stack
- Java (JDK 17+)
- Spring Boot
- Spring Data JPA
- Vaadin (Frontend UI)
- Maven (Build Tool)
- H2 / MySQL (Database)

## Features
- Student management system
- Basic authentication (email/password)
- Assessment tracking
- Performance analytics dashboard
- Placement readiness evaluation
- Suggestion system for improvement
- UI built using Vaadin

## How It Works
- Students log in or register
- Add assessment scores over time
- System tracks performance history
- Analytics and evaluation logic compute readiness
- Suggestions are generated based on weak areas

## Limitations (MVP)
- Passwords are not encrypted
- No role-based access (admin/user)
- Limited analytics depth
- UI-driven (no REST APIs)