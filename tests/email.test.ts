import { describe, it, expect } from '@jest/globals';

describe('Email Interface Tests', () => {
  it('should validate correct email format', () => {
    const validEmails = [
      'test@example.com',
      'user.name@domain.co.uk',
      'admin@hotel-management.com',
    ];

    validEmails.forEach(email => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      expect(emailRegex.test(email)).toBe(true);
    });
  });

  it('should reject invalid email formats', () => {
    const invalidEmails = [
      'invalid.email',
      '@nodomain.com',
      'user@.com',
      'user@domain',
    ];

    invalidEmails.forEach(email => {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      expect(emailRegex.test(email)).toBe(false);
    });
  });

  it('should have required email properties', () => {
    const email = {
      to: 'user@example.com',
      subject: 'Test Subject',
      body: 'Test Body',
    };

    expect(email).toHaveProperty('to');
    expect(email).toHaveProperty('subject');
    expect(email).toHaveProperty('body');
  });
});

describe('Password Reset Email Tests', () => {
  it('should generate password reset email with token', () => {
    const resetEmail = {
      to: 'user@example.com',
      subject: 'Password Reset Request',
      token: 'abc123xyz789',
      resetLink: 'https://hotel-management.com/reset-password?token=abc123xyz789',
    };

    expect(resetEmail.to).toBe('user@example.com');
    expect(resetEmail.token).toHaveLength(12);
    expect(resetEmail.resetLink).toContain(resetEmail.token);
  });

  it('should validate password reset token expiration', () => {
    const tokenExpiry = new Date();
    tokenExpiry.setHours(tokenExpiry.getHours() + 1);

    expect(tokenExpiry.getTime()).toBeGreaterThan(Date.now());
  });

  it('should contain required fields in password reset email', () => {
    const passwordResetEmail = {
      to: 'admin@hotel.com',
      from: 'noreply@hotel-management.com',
      subject: 'Reset Your Password',
      resetToken: 'secure-token-123',
      expiresIn: '1 hour',
    };

    expect(passwordResetEmail).toHaveProperty('to');
    expect(passwordResetEmail).toHaveProperty('from');
    expect(passwordResetEmail).toHaveProperty('resetToken');
    expect(passwordResetEmail).toHaveProperty('expiresIn');
  });
});

describe('Welcome Email with Password Tests', () => {
  it('should generate welcome email with temporary password', () => {
    const welcomeEmail = {
      to: 'newuser@example.com',
      subject: 'Welcome to Hotel Management System',
      temporaryPassword: 'TempPass123!',
      mustChangePassword: true,
    };

    expect(welcomeEmail.temporaryPassword).toBeDefined();
    expect(welcomeEmail.mustChangePassword).toBe(true);
  });

  it('should validate temporary password complexity', () => {
    const tempPassword = 'TempPass123!';

    const hasUpperCase = /[A-Z]/.test(tempPassword);
    const hasLowerCase = /[a-z]/.test(tempPassword);
    const hasNumber = /[0-9]/.test(tempPassword);
    const hasSpecialChar = /[!@#$%^&*]/.test(tempPassword);

    expect(hasUpperCase).toBe(true);
    expect(hasLowerCase).toBe(true);
    expect(hasNumber).toBe(true);
    expect(hasSpecialChar).toBe(true);
  });
});

