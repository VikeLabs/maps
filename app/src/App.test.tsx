import React from 'react';
import {render, screen} from '@testing-library/react';
import App from './App';
import {Service} from '../dist'

test('renders learn react link', () => {
    render(<App/>);
    const linkElement = screen.getByText(/learn react/i);
    expect(linkElement).toBeInTheDocument();
});

test('client was generated prior', () => {
    expect(Service.getPing()).not.toBeUndefined()
})