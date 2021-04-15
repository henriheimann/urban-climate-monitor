import * as fromAuth from './auth.actions';

describe('loginUser', () => {
  it('should return an action', () => {
    expect(fromAuth.loginUser().type).toBe('[Auth] Login User');
  });
});
